package ukma.edu.ua.network.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Builder;

import java.io.IOException;
import java.io.OutputStream;

import static java.util.Objects.nonNull;

@Builder
public class RequestHandler implements HttpHandler {
    private final HttpHandler get;
    private final HttpHandler post;
    private final HttpHandler put;
    private final HttpHandler delete;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equalsIgnoreCase("GET") && nonNull(get)) {
            get.handle(exchange);
        } else if (requestMethod.equalsIgnoreCase("POST") && nonNull(post)) {
            post.handle(exchange);
        } else if (requestMethod.equalsIgnoreCase("PUT") && nonNull(put)) {
            put.handle(exchange);
        } else if (requestMethod.equalsIgnoreCase("DELETE") && nonNull(delete)) {
            delete.handle(exchange);
        } else {
            handleUnsupportedMethod(exchange);
        }
    }

    private void handleUnsupportedMethod(HttpExchange exchange) throws IOException {
        String response = "Unsupported HTTP method";
        sendResponse(exchange, response, 405);
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
