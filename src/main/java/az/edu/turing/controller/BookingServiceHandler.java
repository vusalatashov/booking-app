package az.edu.turing.controller;

import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dto.BookingDto;
import az.edu.turing.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BookingServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceHandler.class);
    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    public BookingServiceHandler(BookingService bookingService, ObjectMapper objectMapper) {
        this.bookingService = bookingService;
        this.objectMapper = objectMapper;
    }

    public void handleCreateBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.debug("Reading JSON input from request...");
            BookingDto bookingDto = objectMapper.readValue(req.getReader(), BookingDto.class);
            logger.debug("JSON input read successfully: {}", bookingDto);

            bookingService.saveBooking(bookingDto);
            logger.debug("Booking saved with ID: {}", bookingDto.getId());

            if (bookingDto.getId() != 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Booking successful");
            } else {
                logger.error("Booking failed, ID is zero");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Booking failed");
            }
        } catch (RuntimeException e) {
            logger.error("Runtime exception occurred", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    public void handleCancelBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long bookingId = Long.parseLong(req.getParameter("id"));
            bookingService.cancelBooking(bookingId);
            logger.debug("Booking cancelled with ID: {}", bookingId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Booking cancelled successfully");
        } catch (RuntimeException e) {
            logger.error("Runtime exception occurred", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    public void handleGetAllBookings(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            logger.debug("Fetching all bookings");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), bookingService.getALLBookings());
        } catch (RuntimeException e) {
            logger.error("Runtime exception occurred while fetching all bookings", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred while fetching all bookings", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    public void handleGetBookingById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long bookingId = Long.parseLong(req.getParameter("id"));
            logger.debug("Fetching booking with ID: {}", bookingId);
            BookingEntity bookingEntity = bookingService.findById(bookingId);
            if (bookingEntity != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), bookingEntity);
            } else {
                logger.warn("No booking found with ID: {}", bookingId);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No booking found with the given ID");
            }
        } catch (RuntimeException e) {
            logger.error("Runtime exception occurred while fetching booking by ID", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred while fetching booking by ID", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
}
