package az.edu.turing.controller;

import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dto.BookingDto;
import az.edu.turing.service.BookingService;

import java.util.List;

public class BookingControllerImpl implements BookingController {
    private final BookingService bookingService;

    public BookingControllerImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @Override
    public void saveBooking(BookingDto bookingDto) {
        bookingService.saveBooking(bookingDto);
    }

    @Override
    public void cancelBooking(long bookingId) {
            bookingService.cancelBooking(bookingId);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingService.getALLBookings();
    }

    @Override
    public BookingEntity findById(long id) {
        return bookingService.findById(id);
    }

    @Override
    public List<BookingDto> getAllBookingsByPassenger(String passengerNames) {
        return bookingService.getAllBookingsByPassenger(passengerNames);
    }

}
