package ukma.edu.ua.network.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Product;
import ukma.edu.ua.network.http.RequestHandler;
import ukma.edu.ua.service.ProductService;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static ukma.edu.ua.network.http.handlers.HttpHelpers.readToString;

public class ProductRequestHandler {
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("/product/([0-9]+)");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ProductRequestHandler() {
    }

    public static HttpHandler create(ProductService productService) {
        return RequestHandler.builder()
                .get(new GetProductHandler(productService))
                .put(new PutProductHandler(productService))
                .post(new PostProductHandler(productService))
                .delete(new DeleteProductHandler(productService))
                .build();
    }

    @RequiredArgsConstructor
    private static class GetProductHandler implements HttpHandler {
        private final ProductService productService;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/json");

            long id;
            try {
                id = HttpHelpers.parseId(exchange.getRequestURI().getPath());
            } catch (IllegalArgumentException e) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            try {
                Product product = productService.getById(id);
                String response = gson.toJson(product);

                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                exchange.getResponseBody().close();
            } catch (NoEntityFoundException e) {
                exchange.sendResponseHeaders(404, 0);
            }
        }
    }

    @RequiredArgsConstructor
    private static class PutProductHandler implements HttpHandler {
        private final ProductService productService;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/json");

            Product updated;
            try {
                updated = gson.fromJson(readToString(exchange.getRequestBody()), Product.class);
            } catch (IOException | JsonSyntaxException e) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            Product product = productService.updateProduct(updated);
            String response = String.valueOf(product.id());

            exchange.sendResponseHeaders(201, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        }
    }

    @RequiredArgsConstructor
    private static class PostProductHandler implements HttpHandler {
        private final ProductService productService;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/json");

            Product product;
            try {
                product = gson.fromJson(readToString(exchange.getRequestBody()), Product.class);
            } catch (IOException | JsonSyntaxException e) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            productService.saveProduct(product);
            String response = String.valueOf(product.id());

            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        }
    }

    @RequiredArgsConstructor
    private static class DeleteProductHandler implements HttpHandler {
        private final ProductService productService;


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/json");

            long id;
            try {
                id = HttpHelpers.parseId(exchange.getRequestURI().getPath());
            } catch (IllegalArgumentException e) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            try {
                Product product = productService.getById(id);
                productService.deleteProduct(product);

                exchange.sendResponseHeaders(204, 0);
            } catch (NoEntityFoundException e) {
                exchange.sendResponseHeaders(404, 0);
            }
        }
    }
}
