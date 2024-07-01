package az.edu.turing.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);

        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(new ServletHolder(new HelloServlet()), "/");
        handler.addServlet(new ServletHolder(new MeServlet()), "/me/*");
        handler.addServlet(new ServletHolder(new GreetingServlet()), "/greeting/*");

        server.setHandler(handler);

        server.start();
        server.join();
    }
}
