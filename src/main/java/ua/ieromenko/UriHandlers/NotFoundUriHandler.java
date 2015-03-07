package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NotFoundUriHandler implements UriHandler {

    @Override
    public FullHttpResponse process(HttpRequest request, StringBuilder buff) {

        buff.append("<!DOCTYPE html>");
        buff.append("<html><body><h1>");
        buff.append("404 NOT FOUND!");
        buff.append("</h1></body></html>");

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                NOT_FOUND,
                Unpooled.copiedBuffer(buff.toString(), CharsetUtil.UTF_8)
        );
        return response;
    }
}
