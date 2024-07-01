package az.edu.turing.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

public class MeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String name = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : "Unknown";

        resp.setContentType("text/plain");
        try (OutputStream os = resp.getOutputStream()) {
            os.write(name.getBytes());
        }
    }
}
