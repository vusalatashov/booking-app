package az.edu.turing.dao.impl;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.database.JdbcConnection;
import az.edu.turing.database.SQLQueries;
import az.edu.turing.service.LoggerService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingPostgresDao implements BookingDao {

    @Override
    public void save(BookingEntity entity) {
        try (Connection conn = JdbcConnection.getConnection()) {
            conn.setAutoCommit(false);

            if (!isFlightExists(conn, entity.getFlightId())) {
                throw new SQLException("Flight ID does not exist.");
            }

            long bookingId = createBooking(conn, entity.getFlightId());
            entity.setId(bookingId);

            for (String passengerName : entity.getPassengerNames()) {
                long passengerId = addPassenger(conn, passengerName);
                addBookingPassenger(conn, bookingId, passengerId);
            }

            adjustSeatCount(conn, entity.getFlightId(), -entity.getPassengerNames().size());

            conn.commit();
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to save booking: " + e.getMessage(), e);
            throw new RuntimeException("Booking save failed", e);
        }
    }

    private boolean isFlightExists(Connection conn, long flightId) throws SQLException {
        String checkFlightSQL = "SELECT id FROM flight WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkFlightSQL)) {
            stmt.setLong(1, flightId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        }
    }

    private long createBooking(Connection conn, long flightId) throws SQLException {
        try (PreparedStatement bookingStmt = conn.prepareStatement(SQLQueries.CREATE_BOOKING, Statement.RETURN_GENERATED_KEYS)) {
            bookingStmt.setLong(1, flightId);
            bookingStmt.executeUpdate();
            ResultSet generatedKeys = bookingStmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                conn.rollback();
                throw new SQLException("Booking creation failed, no ID obtained.");
            }
        }
    }

    private long addPassenger(Connection conn, String passengerName) throws SQLException {
        try (PreparedStatement passengerStmt = conn.prepareStatement(SQLQueries.ADD_PASSENGER, Statement.RETURN_GENERATED_KEYS)) {
            passengerStmt.setString(1, passengerName);
            passengerStmt.executeUpdate();
            ResultSet generatedKeys = passengerStmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Passenger creation failed, no ID obtained.");
            }
        }
    }

    private void addBookingPassenger(Connection conn, long bookingId, long passengerId) throws SQLException {
        try (PreparedStatement bookingPassengerStmt = conn.prepareStatement(SQLQueries.ADD_BOOKING_PASSENGER)) {
            bookingPassengerStmt.setLong(1, bookingId);
            bookingPassengerStmt.setLong(2, passengerId);
            bookingPassengerStmt.executeUpdate();
        }
    }

    private void adjustSeatCount(Connection conn, long flightId, int seatAdjustment) throws SQLException {
        try (PreparedStatement adjustSeatCountStmt = conn.prepareStatement(seatAdjustment > 0 ? SQLQueries.INCREASE_SEAT_COUNT : SQLQueries.DECREASE_SEAT_COUNT)) {
            adjustSeatCountStmt.setInt(1, Math.abs(seatAdjustment));
            adjustSeatCountStmt.setLong(2, flightId);
            adjustSeatCountStmt.executeUpdate();
        }
    }

    @Override
    public List<BookingEntity> findAll() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.GET_ALL_BOOKING);
             ResultSet result = query.executeQuery()) {
            while (result.next()) {
                long id = result.getLong(1);
                long flightId = result.getLong(2);
                String passengerNames = result.getString(3);
                bookingEntities.add(new BookingEntity(id, flightId, List.of(passengerNames.split(","))));
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find all bookings: " + e.getMessage(), e);
        }
        return bookingEntities;
    }

    @Override
    public BookingEntity findById(long id) {
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.GET_BOOKING_BY_ID)) {
            query.setLong(1, id);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                long bookingId = resultSet.getLong(1);
                long flightId = resultSet.getLong(2);
                String fullNames = resultSet.getString(3);
                return new BookingEntity(bookingId, flightId, List.of(fullNames.split(",")));
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find booking by ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void cancelBooking(long bookingId) {
        try (Connection conn = JdbcConnection.getConnection()) {
            conn.setAutoCommit(false);

            long flightId = getFlightIdByBookingId(conn, bookingId);
            int passengerCount = getPassengerCountByBookingId(conn, bookingId);

            try (PreparedStatement deleteBookingPassengerStmt = conn.prepareStatement(SQLQueries.DELETE_BOOKING_PASSENGER);
                 PreparedStatement deleteBookingStmt = conn.prepareStatement(SQLQueries.DELETE_BOOKING)) {

                deleteBookingPassengerStmt.setLong(1, bookingId);
                deleteBookingStmt.setLong(1, bookingId);
                deleteBookingPassengerStmt.executeUpdate();
                deleteBookingStmt.executeUpdate();

                adjustSeatCount(conn, flightId, passengerCount);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                LoggerService.logger.error("Failed to cancel booking: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to cancel booking: " + e.getMessage(), e);
        }
    }

    private int getPassengerCountByBookingId(Connection conn, long bookingId) throws SQLException {
        String getPassengerCountSQL = "SELECT COUNT(*) FROM booking_passenger WHERE booking_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getPassengerCountSQL)) {
            stmt.setLong(1, bookingId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve passenger count for booking ID: " + bookingId);
            }
        }
    }

    @Override
    public List<BookingEntity> findByFullName(String passengerName) {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.GET_BOOKING_BY_FULLNAME)) {
            query.setString(1, passengerName);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                long bookingId = result.getLong(1);
                long flightId = result.getLong(2);
                String fullNames = result.getString(3);
                bookingEntities.add(new BookingEntity(bookingId, flightId, List.of(fullNames.split(","))));
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find booking by full name: " + e.getMessage(), e);
        }
        return bookingEntities;
    }

    private long getFlightIdByBookingId(Connection conn, long bookingId) throws SQLException {
        String getFlightIdSQL = "SELECT flight_id FROM booking WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getFlightIdSQL)) {
            stmt.setLong(1, bookingId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        throw new SQLException("Flight ID not found for booking ID: " + bookingId);
    }
}
