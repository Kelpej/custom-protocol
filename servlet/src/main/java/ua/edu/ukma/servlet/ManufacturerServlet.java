package ua.edu.ukma.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ukma.edu.ua.model.entity.Manufacturer;
import ukma.edu.ua.service.ManufacturerService;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "manufacturers", urlPatterns = "/manufacturer/*")
public class ManufacturerServlet extends HttpServlet {
    private final transient ManufacturerService manufacturerService = ApplicationContext.manufacturerService;
    private final transient Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        if (Helpers.hasIdInPath("manufacturer", req.getPathInfo())) {
            Optional<Long> manufacturerId = Helpers.extractIdFromPath(req.getPathInfo());

            if (manufacturerId.isPresent()) {
                try {
                    Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerId.get());
                    String jsonResponse = gson.toJson(manufacturer);

                    resp.getWriter().write(jsonResponse);
                    resp.getWriter().close();
                } catch (NoEntityFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }

        String response = gson.toJson(manufacturerService.getAll());
        resp.getWriter().write(response);
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Manufacturer newManufacturer = gson.fromJson(req.getReader(), Manufacturer.class);
            manufacturerService.addManufacturer(newManufacturer);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Manufacturer updatedManufacturer = gson.fromJson(req.getReader(), Manufacturer.class);
            manufacturerService.updateManufacturer(updatedManufacturer);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> manufacturerId = Helpers.extractIdFromPath(req.getPathInfo());

        if (manufacturerId.isPresent()) {
            try {
                manufacturerService.deleteManufacturer(manufacturerId.get());
            } catch (NoEntityFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
