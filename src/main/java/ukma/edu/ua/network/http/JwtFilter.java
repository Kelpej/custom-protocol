package ukma.edu.ua.network.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class JwtFilter implements HttpHandler {
    private final HttpHandler next;

    public JwtFilter(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //todo add logic

        next.handle(exchange);
    }
}