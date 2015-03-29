package ua.ieromenko.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.AttributeKey;
import ua.ieromenko.util.ConnectionLogUnit;
import ua.ieromenko.util.LoggingQueue;
import ua.ieromenko.util.RequestsCounter;
import ua.ieromenko.util.StatisticKeeper;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @Author Alexandr Ieromenko on 05.03.15.
 * <p/>
 * Statistics Handler
 */
@Sharable
public class StatisticsHandler extends ChannelTrafficShapingHandler {

    private final AtomicInteger totalConnectionsCounter = new AtomicInteger(0);
    private final AtomicInteger activeConnectionsCounter = new AtomicInteger(0);

    private final ConcurrentHashMap<String, RequestsCounter> requestsCounter = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> redirectionPerURL = new ConcurrentHashMap<>();

    private final LoggingQueue<ConnectionLogUnit> log = new LoggingQueue<>();
    //TODO: Same keys are defined both here and HttpHandler. They should be defined one place. Ideally also used one place.
    private final AttributeKey<ConnectionLogUnit> unit = AttributeKey.valueOf("unit");
    private final AttributeKey<StatisticKeeper> stat = AttributeKey.valueOf("stat");

    public StatisticsHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            totalConnectionsCounter.getAndIncrement();
            activeConnectionsCounter.getAndIncrement();

            HttpRequest request = (HttpRequest) msg;
            String URI = request.getUri();

            // SEND STATISTICS TO HttpHandler
            //TODO: DRY violation. Here you duplicate the knowledge about the /status path with the routing @ HttpHandler.
            if (URI.equals("/status")) {
                //TODO: this StatisticsKeeper is a copy (partially snapshop, partially live) of the state of the StatisticsHandler.
                //TODO: doesn't make much sense to me. I would prefer either to have a global (injected via ServerInitializer?)
                //TODO: shared object, or just to pass this (StatisticsHandler) to the StatusUriHandler.
                StatisticKeeper c = new StatisticKeeper(redirectionPerURL,
                        log, requestsCounter, activeConnectionsCounter.get(), totalConnectionsCounter.get());
                ctx.channel().attr(stat).set(c);
            }

            //IP REQUESTS COUNTER
            //UNIQUE REQUESTS PER IP COUNTER
            String requestIP = (((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
            RequestsCounter c;
            synchronized (requestsCounter) {
                if (!requestsCounter.containsKey(requestIP)) {
                    c = new RequestsCounter(requestIP, URI);
                    requestsCounter.put(requestIP, c);
                } else {
                    c = requestsCounter.get(requestIP).addRequest(URI);
                    requestsCounter.put(requestIP, c);
                }
            }

            //REDIRECTION COUNT
            //TODO: DRY violation re. the redirection pattern. It would probably be more convenient to keep the counter at the Redirect Handler?
            if (URI.matches("/redirect\\?url=\\S*")) {
                String url = URI.substring(URI.indexOf("=") + 1, URI.length());
                synchronized (redirectionPerURL) {
                    if (!redirectionPerURL.containsKey(url)) {
                        redirectionPerURL.put(url, 1);
                    } else {
                        redirectionPerURL.put(url, redirectionPerURL.get(url) + 1);
                    }
                }
            }
        }
        super.channelRead(ctx, msg);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        synchronized (log) {
            ConnectionLogUnit unit1 = ctx.channel().attr(unit).getAndRemove();
            if (unit1 != null) log.add(unit1);
        }
        activeConnectionsCounter.getAndDecrement();
        super.handlerRemoved(ctx);
    }

}
