package ukma.edu.ua.network.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ukma.edu.ua.network.http.RequestHandler;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

public class LoginHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private final Key key;
    private final long timeToExpiration;

    private LoginHandler(Key key, long timeToExpiration) {
        this.key = key;
        this.timeToExpiration = timeToExpiration;
    }

    public static HttpHandler create(Key key, long timeToExpiration) {
        return RequestHandler.builder()
                .post(new LoginHandler(key, timeToExpiration))
                .build();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Date expiration = new Date(System.currentTimeMillis() + timeToExpiration);
        exchange.getResponseHeaders().set("Content-Type", "text/json");

        String token = Jwts.builder()
                .setSubject("stub")
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        logger.info("Token: {}", token);

        byte[] bytes = token.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
