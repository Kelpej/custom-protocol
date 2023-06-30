package ua.edu.ukma.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ukma.edu.ua.model.entity.Product;
import ukma.edu.ua.service.ProductService;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "products", urlPatterns = "/product/*")
public class ProductServlet extends HttpServlet {
    private transient final ProductService productService = ApplicationContext.productService;
    private final transient Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String path = req.getPathInfo();
        if (Helpers.hasIdInPath("product", path)) {
            Optional<Long> productId = Helpers.extractIdFromPath(path);

            if (productId.isPresent()) {
                try {
                    Product product = productService.getById(productId.get());
                    String jsonResponse = gson.toJson(product);

                    resp.getWriter().write(jsonResponse);
                    resp.getWriter().close();
                } catch (NoEntityFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        List<Product> products = productService.getAll();
        System.out.println(products);
        String response = gson.toJson(products);
        resp.getWriter().write(response);
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product newProduct = gson.fromJson(req.getReader(), Product.class);
            productService.saveProduct(newProduct);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product newProduct = gson.fromJson(req.getReader(), Product.class);
            productService.updateProduct(newProduct);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> productId = Helpers.extractIdFromPath(req.getPathInfo());

        if (productId.isPresent()) {
            try {
                productService.deleteProduct(productId.get());
            } catch (NoEntityFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
