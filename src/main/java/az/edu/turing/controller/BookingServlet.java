package az.edu.turing.controller;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.impl.BookingPostgresDao;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.BookingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BookingServlet extends HttpServlet {
    private final BookingServiceHandler bookingServiceHandler;

    public BookingServlet() {
        BookingDao bookingDao = new BookingPostgresDao();
        BookingService bookingService = new BookingServiceImpl(bookingDao);
        ObjectMapper objectMapper = new ObjectMapper();
        this.bookingServiceHandler = new BookingServiceHandler(bookingService, objectMapper);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            bookingServiceHandler.handleGetBookingById(request, response);
        } else {
            bookingServiceHandler.handleGetAllBookings(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        bookingServiceHandler.handleCreateBooking(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        bookingServiceHandler.handleCancelBooking(request, response);
    }
}
