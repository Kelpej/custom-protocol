package ukma.edu.ua;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import ukma.edu.ua.network.http.JwtFilter;
import ukma.edu.ua.network.http.handlers.LoginHandler;
import ukma.edu.ua.network.http.handlers.ProductRequestHandler;
import ukma.edu.ua.persistent.Hibernate;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ManufacturerDao;
import ukma.edu.ua.persistent.impl.ProductDao;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.ProductService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.Key;

@RequiredArgsConstructor
public class Application {

    public static void main(String[] args) throws IOException {
        ProductDao productDao = new ProductDao(Hibernate.getSessionFactory());
        GroupDao groupDao = new GroupDao(Hibernate.getSessionFactory());
        ManufacturerDao manufacturerDao = new ManufacturerDao(Hibernate.getSessionFactory());

        ProductService productService = new ProductService(productDao, groupDao);
        GroupService groupService = new GroupService(groupDao);

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        int oneHourInMillis = 1000 * 3600;

        HttpServer http = HttpServer.create(new InetSocketAddress(8000), 0);
        http.createContext("/api/product", authenticated(ProductRequestHandler.create(productService), key));
        http.createContext("/api/login", LoginHandler.create(key, oneHourInMillis));
        http.start();
    }

    private static HttpHandler authenticated(HttpHandler handler, Key key) {
        return new JwtFilter(key, handler);
    }
}
