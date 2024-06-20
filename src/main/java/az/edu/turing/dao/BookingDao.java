package az.edu.turing.dao;

import az.edu.turing.dao.entity.BookingEntity;

import java.util.List;

public interface BookingDao {
    void save(BookingEntity entity);

    List<BookingEntity> findAll();

    BookingEntity findById(long id);

    void cancelBooking(long bookingId);

    List<BookingEntity> findByFullName(String passengerNames);


}
