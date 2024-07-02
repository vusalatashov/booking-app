package az.edu.turing.controller;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.impl.FlightPostgresDao;
import az.edu.turing.service.FlightService;
import az.edu.turing.service.FlightServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FlightServlet extends HttpServlet {
    private final FlightServiceHandler flightServiceHandler;

    public FlightServlet() {
        FlightDao flightDao = new FlightPostgresDao();
        FlightService flightService = new FlightServiceImpl(flightDao);
        ObjectMapper objectMapper = createObjectMapper();
        this.flightServiceHandler = new FlightServiceHandler(flightService, objectMapper);
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            flightServiceHandler.handleGetFlightById(request, response);
        } else {
            flightServiceHandler.handleGetAllFlights(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        flightServiceHandler.handleCreateFlight(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        flightServiceHandler.handleCancelFlight(request, response);
    }
}
