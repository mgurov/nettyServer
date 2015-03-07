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

    @Override
    public FullHttpResponse process(HttpRequest request, StringBuilder buff) {

        buff.append("<!DOCTYPE html>");
        buff.append("<html><body><h1>");
        buff.append("Hello World!!");
        buff.append("</h1></body></html>");

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.copiedBuffer(buff.toString(), CharsetUtil.UTF_8)
        );
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
