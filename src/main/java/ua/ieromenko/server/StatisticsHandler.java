package ua.ieromenko.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.AttributeKey;
import ua.ieromenko.util.LoggingQueue;
import ua.ieromenko.wrappers.ConnectionLogUnit;
import ua.ieromenko.wrappers.RequestsCounter;
import ua.ieromenko.wrappers.WrapperOfEverything;

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

    private final AttributeKey<ConnectionLogUnit> unit = AttributeKey.valueOf("unit");
    private final AttributeKey<WrapperOfEverything> stat = AttributeKey.valueOf("stat");

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
            if (URI.equals("/status")) {
                WrapperOfEverything c = new WrapperOfEverything(redirectionPerURL,
                        log, requestsCounter, activeConnectionsCounter, totalConnectionsCounter);
                ctx.channel().attr(stat).set(c);
            }

            //IP REQUESTS COUNTER
            //UNIQUE REQUESTS PER IP COUNTER
            synchronized (requestsCounter) {
                String requestIP = (((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
                RequestsCounter c;
                if (!requestsCounter.containsKey(requestIP)) {
                    c = new RequestsCounter(requestIP, URI);
                    requestsCounter.put(requestIP, c);
                } else {
                    c = requestsCounter.get(requestIP).addRequest(URI);
                    requestsCounter.put(requestIP, c);
                }
            }

            //REDIRECTION COUNT

            if (URI.matches("/redirect\\?url=\\S*")) {
                synchronized (redirectionPerURL) {
                    if (!redirectionPerURL.containsKey(URI)) {
                        redirectionPerURL.put(URI, 1);
                    } else {
                        redirectionPerURL.put(URI, redirectionPerURL.get(URI) + 1);
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
