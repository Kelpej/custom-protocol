package ua.edu.ukma.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ukma.edu.ua.model.entity.Group;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "groups", urlPatterns = "/group/*")
public class GroupServlet extends HttpServlet {
    private final transient GroupService groupService = ApplicationContext.groupService;
    private final transient Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String path = req.getPathInfo();
        if (Helpers.hasIdInPath("group", path)) {
            Optional<Long> groupId = Helpers.extractIdFromPath(path);

            if (groupId.isPresent()) {
                try {
                    Group group = groupService.getGroup(groupId.get());
                    String jsonResponse = gson.toJson(group);

                    resp.getWriter().write(jsonResponse);
                    resp.getWriter().close();
                } catch (NoEntityFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        List<Group> groups = groupService.getAll();
        String response = gson.toJson(groups);
        resp.getWriter().write(response);
        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Group newGroup = gson.fromJson(req.getReader(), Group.class);
            groupService.addGroup(newGroup);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Group updatedGroup = gson.fromJson(req.getReader(), Group.class);
            groupService.updateGroup(updatedGroup);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> groupId = Helpers.extractIdFromPath(req.getPathInfo());

        if (groupId.isPresent()) {
            try {
                groupService.deleteGroup(groupId.get());
            } catch (NoEntityFoundException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
