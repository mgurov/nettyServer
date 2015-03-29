package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class RedirectUriHandler implements UriHandler {

    @Override
    public FullHttpResponse process(HttpRequest request) {
        String requestURI = request.getUri();
        //TODO: wouldn't work correctly if you happend to have any other unexpected query params, e.g. /redirect?timeout=z&url=x
        //TODO: wouldn't work correctly with url-encoded parameters.
        String url = requestURI.substring(requestURI.indexOf("=") + 1, requestURI.length());
        //TODO: here it would be much better to check the already extracted URL and forget about the requestURI.
        if (!requestURI.matches("/redirect\\?url=http\\S*")) url = "http://" + url;
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, url);
        return response;
    }
}
