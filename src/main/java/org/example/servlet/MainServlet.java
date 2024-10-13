package org.example.servlet;

import org.example.controller.PostController;
import org.example.repository.PostRepositoryImpl;
import org.example.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepositoryImpl();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            switch (method) {
                case "GET": {
                    get(path, resp);
                    break;
                }
                case "POST": {
                    post(path, req, resp);
                    break;
                }
                case "DELETE": {
                    delete(path, resp);
                    break;
                }
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void get(String path, HttpServletResponse resp) throws IOException {
        if (isPathEquals(path)) {
            controller.all(resp);

        } else if (isPathMatchesPattern(path)) {
            final var id = getIdByPath(path);
            controller.getById(id, resp);
        }
    }

    private void post(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isPathEquals(path)) {
            controller.save(req.getReader(), resp);
        }
    }

    private void delete(String path, HttpServletResponse resp) throws IOException {
        if (isPathMatchesPattern(path)) {
            final var id = getIdByPath(path);
            controller.removeById(id, resp);
        }
    }

    private long getIdByPath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }

    private boolean isPathMatchesPattern(String path) {
        return path.matches("/api/posts/\\d+");
    }

    private boolean isPathEquals(String path) {
        return path.equals("/api/posts");
    }
}

