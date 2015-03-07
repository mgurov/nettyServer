package ua.ieromenko.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Alexandr Ieromenko on 04.03.15.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    //TrafficCounter stopped because it counts only request`s body bytes
    private final StatisticsHandler statisticsHandler = new StatisticsHandler(0);

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast("codec", new HttpServerCodec());
        p.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        p.addLast("statisticsHandler", statisticsHandler);
        p.addLast("handler", new HttpHandler());
    }

}
