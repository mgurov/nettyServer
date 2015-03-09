package ua.ieromenko.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;
import ua.ieromenko.UriHandlers.*;
import ua.ieromenko.wrappers.ConnectionLogUnit;
import ua.ieromenko.wrappers.WrapperOfEverything;

import java.net.InetSocketAddress;
import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * @Author Alexandr Ieromenko on 04.03.15.
 * <p/>
 * Main HttpRequests handler
 * <p/>
 */
class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private FullHttpRequest request;
    private final StringBuilder buf = new StringBuilder();

    private final AttributeKey<ConnectionLogUnit> unit = AttributeKey.valueOf("unit");
    private final AttributeKey<WrapperOfEverything> stat = AttributeKey.valueOf("stat");

    private ConnectionLogUnit logUnit = null;
    private int sentBytes;
    private int receivedBytes;
    private long time;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        time = System.nanoTime(); //channel is connected and ready

        String requestIP = (((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
        logUnit = new ConnectionLogUnit(requestIP, new Date());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        if (httpRequest != null) {
            this.request = httpRequest;
            String URI = request.getUri();

            //let`s handle this request
            UriHandler handler;
            if (URI.equals("/hello")) handler = new HelloUriHandler();
            else if (URI.matches("/redirect\\?url=\\S*")) handler = new RedirectUriHandler();
            else if (URI.equals("/status")) {
                //read statistics that StatisticsHandler has already prepared
                WrapperOfEverything wrapper = ctx.channel().attr(stat).getAndRemove();
                handler = new StatusUriHandler(wrapper);
            } else handler = new NotFoundUriHandler();

            //send response
            FullHttpResponse response = handler.process(request, buf);
            //close the connection immediately because no more requests can be sent from the browser
            response.headers().set(CONTENT_TYPE, UriHandler.CONTENT_TYPE);
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);

            // do some statistics
            logUnit.setURI(URI); //
            ByteBuf buffer = Unpooled.copiedBuffer(httpRequest.toString().getBytes());
            receivedBytes = buffer.readableBytes() + httpRequest.content().readableBytes();
            sentBytes = response.content().writerIndex();
            logUnit.setReceivedBytes(receivedBytes);
            logUnit.setSentBytes(sentBytes);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //calculate speed
        //if there is another formula i can change that
        if (request != null) {
            long time0 = System.nanoTime() - time;
            double time1 = time0 / (double) 1000000000;
            long speed = Math.round((sentBytes + receivedBytes) / time1);
            logUnit.setSpeed(speed);
        }
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //send log of this connection before disconnecting from channel
        ctx.channel().attr(unit).set(logUnit);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
