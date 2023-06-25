package ukma.edu.ua;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import ukma.edu.ua.network.http.JwtFilter;
import ukma.edu.ua.network.http.handlers.ProductRequestHandler;
import ukma.edu.ua.persistent.Hibernate;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ManufacturerDao;
import ukma.edu.ua.persistent.impl.ProductDao;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.ProductService;

import java.io.IOException;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class Application {

    public static void main(String[] args) throws IOException {
        ProductDao productDao = new ProductDao(Hibernate.getSessionFactory());
        GroupDao groupDao = new GroupDao(Hibernate.getSessionFactory());
        ManufacturerDao manufacturerDao = new ManufacturerDao(Hibernate.getSessionFactory());

        ProductService productService = new ProductService(productDao, groupDao);
        GroupService groupService = new GroupService(groupDao);

        HttpServer http = HttpServer.create(new InetSocketAddress(8000), 0);
        http.createContext("/api/product", authenticated(ProductRequestHandler.create(productService)));
        http.start();
    }

    private static HttpHandler authenticated(HttpHandler handler) {
        return new JwtFilter(handler);
    }
}
