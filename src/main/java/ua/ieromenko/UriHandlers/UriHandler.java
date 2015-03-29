package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

public interface UriHandler{
    /**
     * Create a response
     *
     * @param request
     * @return
     */
    FullHttpResponse process(HttpRequest request);


}
