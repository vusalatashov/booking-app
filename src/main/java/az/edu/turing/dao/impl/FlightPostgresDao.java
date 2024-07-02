package az.edu.turing.dao.impl;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dao.entity.FlightEntity;
import az.edu.turing.database.JdbcConnection;
import az.edu.turing.database.SQLQueries;
import az.edu.turing.service.LoggerService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightPostgresDao implements FlightDao {

    @Override
    public void save(FlightEntity entity) {
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.ADD_FLIGHT_SQL)) {
            conn.setAutoCommit(false);

            query.setString(1, entity.getOrigin().name().toUpperCase());
            query.setString(2, entity.getDestination().name().toUpperCase());
            query.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            query.setInt(4, entity.getNumOfSeats());
            query.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to save flight: " + e.getMessage(), e);
        }
    }

    @Override
    public FlightEntity findById(long id) {
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.FIND_FLIGHT_BY_ID_SQL)) {
            query.setLong(1, id);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                return new FlightEntity(flightId, Cities.fromString(origin), Cities.fromString(destination), departureTime, numOfSeats);
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find flight by ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<FlightEntity> findAll() {
        List<FlightEntity> flightEntities = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.FIND_ALL_FLIGHTS_SQL);
             ResultSet resultSet = query.executeQuery()) {
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntities.add(new FlightEntity(flightId, Cities.fromString(origin), Cities.fromString(destination), departureTime, numOfSeats));
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find all flights: " + e.getMessage(), e);
        }
        return flightEntities;
    }

    @Override
    public void cancelFlight(long flightId) {
        try (Connection conn = JdbcConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // First, delete related entries from booking_passenger table
                try (PreparedStatement deleteBookingPassengersQuery = conn.prepareStatement(SQLQueries.DELETE_BOOKING_PASSENGER_BY_FLIGHT_ID)) {
                    deleteBookingPassengersQuery.setLong(1, flightId);
                    deleteBookingPassengersQuery.executeUpdate();
                }

                // Delete related entries from booking table
                try (PreparedStatement deleteBookingsQuery = conn.prepareStatement(SQLQueries.DELETE_BOOKINGS_BY_FLIGHT_ID)) {
                    deleteBookingsQuery.setLong(1, flightId);
                    deleteBookingsQuery.executeUpdate();
                }

                // Finally, delete the flight
                try (PreparedStatement query = conn.prepareStatement(SQLQueries.CANCEL_FLIGHT_SQL)) {
                    query.setLong(1, flightId);
                    query.executeUpdate();
                    conn.commit();
                } catch (SQLException e) {
                    LoggerService.logger.error("Failed to cancel flight: " + e.getMessage(), e);
                    conn.rollback();
                }
            } catch (SQLException e) {
                LoggerService.logger.error("Failed to cancel flight: " + e.getMessage(), e);
                conn.rollback();
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to cancel flight: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FlightEntity> findByOrigin(String origin) {
        List<FlightEntity> flightEntities = new ArrayList<>();
        try (Connection conn = JdbcConnection.getConnection();
             PreparedStatement query = conn.prepareStatement(SQLQueries.FIND_FLIGHT_BY_ORIGIN_SQL)) {
            query.setString(1, origin);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String originCity = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntities.add(new FlightEntity(flightId, Cities.fromString(originCity), Cities.fromString(destination), departureTime, numOfSeats));
            }
        } catch (SQLException e) {
            LoggerService.logger.error("Failed to find flights by origin: " + e.getMessage(), e);
        }
        return flightEntities;
    }
}
