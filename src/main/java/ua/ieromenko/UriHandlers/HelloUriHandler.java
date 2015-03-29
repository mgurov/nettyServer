package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;


import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HelloUriHandler implements UriHandler {
    private static final int TIMEOUT = 10000;
    private static final String ANSWER_HELLO_WORLD = "<!DOCTYPE html><html><body><h1>Hello World!!!</h1></body></html>";

    @Override
    public FullHttpResponse process(HttpRequest request) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.copiedBuffer(ANSWER_HELLO_WORLD, CharsetUtil.UTF_8)
        );
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
