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
    private static final String ANSWER_NOT_FOUND = "<!DOCTYPE html><html><body><h1>404 NOT FOUND!</h1></body></html>";

    @Override
    public FullHttpResponse process(HttpRequest request) {
        return new DefaultFullHttpResponse(
                HTTP_1_1, NOT_FOUND, Unpooled.copiedBuffer(ANSWER_NOT_FOUND, CharsetUtil.UTF_8)
        );
    }
}
