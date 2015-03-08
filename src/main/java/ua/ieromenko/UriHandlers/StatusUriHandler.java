package ua.ieromenko.UriHandlers;

/**
 * @Author Alexandr Ieromenko on 03.03.15.
 */

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import ua.ieromenko.wrappers.ConnectionLogUnit;
import ua.ieromenko.wrappers.RequestsCounter;
import ua.ieromenko.wrappers.WrapperOfEverything;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class StatusUriHandler implements UriHandler {
    private final WrapperOfEverything stat;

    public StatusUriHandler(WrapperOfEverything stat) {
        this.stat = stat;
    }

    @Override
    public FullHttpResponse process(HttpRequest request, StringBuilder buff) {

        buff.append("<!DOCTYPE html>");
        buff.append("<html>");
        buff.append("<head>");
        buff.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n");
        buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">\n");
        buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css\">\n");
        buff.append("</head>");

        buff.append("<body>");
        buff.append("<div style=\"margin: 0 0 0 25px  \">");
        buff.append("<h2>SERVER STATISTICS</h2>");

        // Total connections
        buff.append("<h4>Total connections: ").append(stat.getConnectionsCounter()).append("</h4>");
        // Active connections
        buff.append("<h4>Active connections: ").append(stat.getActiveConnectionsCounter()).append("</h4>");

        // Unique Requests per one IP table
        buff.append("<h4>Unique requests per IP :</h4>");
        buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 30%;\"><tbody>");
        buff.append("<tr><th>").append(" IP ").append("</th><th>").append("requests").append("</th></tr>");
        for (Map.Entry<String, RequestsCounter> pair : stat.getRequestsCounter().entrySet()) {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            buff.append("</td><td>");
            buff.append(pair.getValue().getCountOfUniqueRequests());
            buff.append("</td></tr>");
        }
        buff.append("</tbody></table>");

        //Counter of the requests per 1 IP
        buff.append("<h4>IP requests counter :</h4>");
        buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 30%;\"><tbody>");
        buff.append("<tr><th>").append(" IP ").append("</th><th>").append("requests")
                .append("</th><th>").append(" last request ").append("</th></tr>");
        for (Map.Entry<String, RequestsCounter> pair : stat.getRequestsCounter().entrySet()) {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            buff.append("</td><td>");
            buff.append(pair.getValue().getConnectionsCounter());
            buff.append("</td><td>");
            buff.append(pair.getValue().getLastConnectionDate());
            buff.append("</td></tr>");
        }
        buff.append("</tbody></table>");

        //Counter of the redirection per URL
        buff.append("<h4>URL redirection counter :</h4>");
        buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 40%;\"><tbody>");
        buff.append("<tr><th class=\"col-md-4\">").append(" URL ")
                .append("</th><th class=\"col-md-1\">").append(" count ").append("</th></tr>");
        for (Map.Entry<String, Integer> pair : stat.getRedirectionPerURL().entrySet()) {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            buff.append("</td><td>");
            buff.append(pair.getValue());
            buff.append("</td></tr>");
        }
        buff.append("</tbody></table>");

        //Connections log
        buff.append("<h4>Connections log :</h4>");
        buff.append("<table class=\"table table-hover table-bordered table-striped\" style=\"width: 70%;\"><tbody>");
        buff.append("<tr><th class=\"col-md-1\">").append("IP ")
                .append("</th><th class=\"col-md-3\">").append("URI ")
                .append("</th><th class=\"col-md-3\">").append("timestamp ")
                .append("</th><th class=\"col-md-1\">").append("sent bytes ")
                .append("</th><th class=\"col-md-1\">").append("received bytes ")
                .append("</th><th class=\"col-md-1\">").append("speed (Bytes/Sec)")
                .append("</th></tr>");
        buff.append("</tbody>");
        for (ConnectionLogUnit c : stat.getLog()) {
            buff.append("<tr><td>");
            buff.append(c.getIP()).append("</td><td>");
            buff.append(c.getURI()).append("</td><td>");
            buff.append(c.getTimeStamp()).append("</td><td>");
            buff.append(c.getSentBytes()).append("</td><td>");
            buff.append(c.getReceivedBytes()).append("</td><td>");
            buff.append(c.getSpeed()).append("</td></tr>");
        }
        buff.append("</tbody></table></div></body></html>");


        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.copiedBuffer(buff.toString(), CharsetUtil.UTF_8)
        );
        return response;
    }
}
