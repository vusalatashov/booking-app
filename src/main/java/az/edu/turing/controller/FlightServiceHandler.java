package az.edu.turing.controller;

import az.edu.turing.dto.FlightDto;
import az.edu.turing.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FlightServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(FlightServiceHandler.class);
    private final FlightService flightService;
    private final ObjectMapper objectMapper;

    public FlightServiceHandler(FlightService flightService, ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.objectMapper = objectMapper;
    }

    public void handleGetFlightById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long flightId = Long.parseLong(req.getParameter("id"));
            FlightDto flightDto = flightService.getFlightById(flightId);
            logger.debug("Flight found: {}", flightDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(flightDto));
        } catch (NumberFormatException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid flight ID format", e);
        } catch (RuntimeException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!", e);
        }
    }

    public void handleGetAllFlights(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.debug("Getting all flights...");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(flightService.getAllFlights()));
        } catch (RuntimeException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!", e);
        }
    }

    public void handleCreateFlight(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.debug("Reading JSON input from request...");
            FlightDto flightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            logger.debug("JSON input read successfully: {}", flightDto);
            flightService.saveFlight(flightDto);
            logger.debug("Flight saved with ID: {}", flightDto.getId());
            if (flightDto.getId() != 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Flight saved successfully");
            } else {
                handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Flight save failed, ID is zero", null);
            }
        } catch (RuntimeException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!", e);
        }
    }

    public void handleCancelFlight(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long flightId = Long.parseLong(req.getParameter("id"));
            flightService.cancelFlight(flightId);
            logger.debug("Flight cancelled with ID: {}", flightId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Flight cancelled successfully");
        } catch (NumberFormatException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid flight ID format", e);
        } catch (RuntimeException e) {
            handleException(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            handleException(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!", e);
        }
    }

    private void handleException(HttpServletResponse resp, int statusCode, String message, Exception e) throws IOException {
        if (e != null) {
            logger.error(message, e);
        } else {
            logger.error(message);
        }
        resp.sendError(statusCode, message);
    }
}
