package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

//TODO: there's nothing really special to URI in this interface. It could be well called HttpHandler, and what is now
public interface UriHandler{
    /**
     * Create a response
     *
     * @param request
     * @return
     */
    FullHttpResponse process(HttpRequest request);


}
