package az.edu.turing.controller;

import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dto.BookingDto;

import java.util.List;

public interface BookingController {
    void saveBooking(BookingDto bookingDto);

    void cancelBooking(long bookingId);

    List<BookingDto> getAllBookings();

    BookingEntity findById(long id);

    List<BookingDto> getAllBookingsByPassenger(String passengerNames);
}
