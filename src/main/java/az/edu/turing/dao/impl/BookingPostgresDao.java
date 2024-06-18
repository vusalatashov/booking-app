package az.edu.turing.dao.impl;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.service.LoggerService;

import java.sql.*;
import java.util.List;

public class BookingPostgresDao implements BookingDao {
    public static final String createBooking="insert into booking(flight_id)"+
    "values(?);";
    public static final String addPassenger="insert into passenger(full_name)"+
    "values(?);";
    public static final String addBookingPassenger="insert into booking_passenger(booking_id,passenger_id)"+
    "values(?,?);";

    @Override
    public void save(BookingEntity entity) {
        Connection conn = null;
        try{
            conn= DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");

            PreparedStatement query=conn.prepareStatement(createBooking);

            query.setLong(1,entity.getFlight_id());
            query.executeUpdate();
            ResultSet resultSet=query.getGeneratedKeys();
            for(String passenger:entity.getPassengerNames()){

                PreparedStatement query1=conn.prepareStatement(addPassenger);
                query1.setString(1,passenger);
                PreparedStatement query2=conn.prepareStatement(addBookingPassenger);
                query2.setLong(1,entity.getFlight_id());
            }

        }catch (SQLException e){
            LoggerService.logger.error(e.getMessage());
        }
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

    @Override
    public void update(BookingEntity entity) {

    }

}
