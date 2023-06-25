package ukma.edu.ua.network.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.util.Date;

public class JwtFilter implements HttpHandler {
    private final Key key;
    private final HttpHandler next;

    public JwtFilter(Key key, HttpHandler next) {
        this.key = key;
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getRequestHeaders();
        String authHeader = headers.getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (validateToken(token)) {
                next.handle(exchange);
            } else {
                sendUnauthorized(exchange);
            }
        } else {
            sendUnauthorized(exchange);
        }
    }

    private boolean validateToken(String token) {
        if (token != null) {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();

            Jws<Claims> claims = parser.parseClaimsJws(token);
            Claims body = claims.getBody();

            return new Date().before(body.getExpiration());
        }

        return false;
    }

    private void sendUnauthorized(HttpExchange exchange) throws IOException {
        String message = "Unauthorized";
        exchange.sendResponseHeaders(401, message.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(message.getBytes());
        outputStream.close();
    }
}