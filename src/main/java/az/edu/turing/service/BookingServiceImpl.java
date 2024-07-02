package az.edu.turing.service;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;

    public BookingServiceImpl(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    @Override
    public void saveBooking(BookingDto bookingDto) {
        BookingEntity bookingEntity = new BookingEntity(bookingDto.getFlightId(), bookingDto.getPassengerNames());
        bookingDao.save(bookingEntity);
        bookingDto.setId(bookingEntity.getId()); // Ensure ID is set in BookingDto
    }

    @Override
    public void cancelBooking(long bookingId) {
        bookingDao.cancelBooking(bookingId);
    }

    @Override
    public List<BookingDto> getALLBookings() {
        return bookingDao.findAll().stream()
                .map(booking -> new BookingDto(booking.getId(), booking.getFlightId(), booking.getPassengerNames()))
                .collect(Collectors.toList());
    }

    @Override
    public BookingEntity findById(long bookingId) {
        return bookingDao.findById(bookingId);
    }

    @Override
    public List<BookingDto> getAllBookingsByPassenger(String passengerName) {
        return bookingDao.findByFullName(passengerName).stream()
                .map(booking -> new BookingDto(booking.getId(), booking.getFlightId(), booking.getPassengerNames()))
                .collect(Collectors.toList());
    }
}
