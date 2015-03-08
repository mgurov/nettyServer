package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

public interface UriHandler{

    FullHttpResponse process(HttpRequest request, StringBuilder buff);

    String CONTENT_TYPE = "text/html; charset=UTF-8";

}
