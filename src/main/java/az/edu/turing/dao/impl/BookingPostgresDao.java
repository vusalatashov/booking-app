package az.edu.turing.dao.impl;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.database.JdbcConnection;
import az.edu.turing.service.LoggerService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingPostgresDao implements BookingDao {
    public static final String createBooking = "insert into booking(flight_id)" +
            "values(?);";
    public static final String addPassenger = "insert into passenger(full_name)" +
            "values(?);";
    public static final String addBookingPassenger = "insert into booking_passenger(booking_id,passenger_id)" +
            "values(?,?);";
    public static final String getBookingById = "SELECT booking.id,booking.flight_id,string_agg(full_name, ',') as full_names\n" +
            "            from booking\n" +
            "            join booking_passenger bp on booking.id = bp.booking_id\n" +
            "            join passenger p on p.id = bp.passenger_id\n" +
            "            where booking.id=?\n" +
            "            group by booking.id;";
    public static final String getAllBooking = "SELECT booking.id,booking.flight_id,string_agg(full_name, ',') as full_names\n" +
            "            from booking\n" +
            "            join booking_passenger bp on booking.id = bp.booking_id\n" +
            "            join passenger p on p.id = bp.passenger_id group by booking.id;";
    public static final String getBookingByFullName="SELECT booking.id, booking.flight_id, string_agg(full_name, ',') as full_names\n" +
            "from booking\n" +
            "         join booking_passenger bp on booking.id = bp.booking_id\n" +
            "         join passenger p on p.id = bp.passenger_id\n" +
            "where booking.id IN\n" +
            "      (SELECT booking.id\n" +
            "       FROM booking\n" +
            "                join booking_passenger b on booking.id = b.booking_id\n" +
            "                JOIN passenger p2 on p2.id = b.passenger_id\n" +
            "       WHERE p2.full_name = ?) group by booking.id;";

    public static final String deleteBooking_PassengerSQL="delete from booking_passenger  where booking_id=?;";
    public static final String deleteBookingSQL="delete from booking  where id=?;";


    @Override
    public void save(BookingEntity entity) {
        try (Connection conn=JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(createBooking, Statement.RETURN_GENERATED_KEYS)) {
            query.setLong(1, entity.getFlightId());
            query.executeUpdate();
            ResultSet generatedKeys = query.getGeneratedKeys();
            if (generatedKeys.next()) {
                long bookingId = generatedKeys.getLong(1);
                for (String passengerName : entity.getPassengerNames()) {
                    PreparedStatement queryPassenger = conn.prepareStatement(addPassenger, Statement.RETURN_GENERATED_KEYS);
                    queryPassenger.setString(1, passengerName);
                    queryPassenger.executeUpdate();
                    ResultSet passengerKeys = queryPassenger.getGeneratedKeys();
                    if (passengerKeys.next()) {
                        long passengerId = passengerKeys.getLong(1);
                        PreparedStatement queryBookingPassenger = conn.prepareStatement(addBookingPassenger);
                        queryBookingPassenger.setLong(1, bookingId);
                        queryBookingPassenger.setLong(2, passengerId);
                        queryBookingPassenger.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
        }
    }

    @Override
    public List<BookingEntity> findAll() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");
            PreparedStatement query = conn.prepareStatement(getAllBooking);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                long id = result.getLong(1);
                long flightId = result.getLong(2);
                String passengerName = result.getString(3);
                bookingEntities.add(new BookingEntity(id, flightId, List.of(passengerName.split(","))));
            }
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
        }
        return bookingEntities;
    }

    @Override
    public BookingEntity findById(long id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");
            PreparedStatement query = conn.prepareStatement(getBookingById);
            query.setLong(1, id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long bookingId = resultSet.getLong(1);
                long flightId = resultSet.getLong(2);
                String fullName = resultSet.getString(3);
                return new BookingEntity(bookingId, flightId, List.of(fullName.split(",")));
            }
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());

        }
        return null;
    }

    @Override
    public void cancelBooking(long bookingId) {
        Connection conn=null;
        try{
            conn=DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");
            PreparedStatement query=conn.prepareStatement(deleteBooking_PassengerSQL);
            PreparedStatement query1= conn.prepareStatement(deleteBookingSQL);
            query.setLong(1,bookingId);
            query1.setLong(1,bookingId);

            query.executeUpdate();
            query1.executeUpdate();

        }catch(SQLException e){
            LoggerService.logger.error(e.getMessage());

        }

    }

    @Override
    public List<BookingEntity> findByFullName(String passengerNames) {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        Connection conn = null;
        try{
            conn=DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");
            PreparedStatement query = conn.prepareStatement(getBookingByFullName);
            query.setString(1,passengerNames);
            ResultSet result=query.executeQuery();
            while(result.next()){
                long bookingId=result.getLong(1);
                long flightId=result.getLong(2);
                String fullName=result.getString(3);

                bookingEntities.add(new BookingEntity(bookingId,flightId,List.of(fullName.split(","))));
            }



        }
        catch(SQLException e){
            LoggerService.logger.error(e.getMessage());

        }

        return bookingEntities;
    }


}
