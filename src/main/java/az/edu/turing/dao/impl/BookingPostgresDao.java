package az.edu.turing.dao.impl;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.entity.BookingEntity;

import java.util.List;

public class BookingPostgresDao implements BookingDao {

    @Override
    public void save(BookingEntity entity) {

    }

    @Override
    public List<BookingEntity> findAll() {
        return List.of();
    }

    @Override
    public BookingEntity findById(long id) {
        return null;
    }

    @Override
    public void cancelBooking(long bookingId) {

    }

    @Override
    public List<BookingEntity> findByFullName(List<String> passengerNames) {
        return List.of();
    }

}
