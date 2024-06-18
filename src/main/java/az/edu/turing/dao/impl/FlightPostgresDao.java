package az.edu.turing.dao.impl;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dao.entity.FlightEntity;
import az.edu.turing.service.LoggerService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightPostgresDao implements FlightDao {
    private static final String ADD_FLIGHT_SQL = "insert into flight(origin,destination,departure_time,free_seats)\n" +
            "values (?,?,?,?);";
    private static final String findFlightById = "select *from flight where id=?;";
    private static final String findAllFlightSQL = "select *from flight;";
    private static final String cancelFlightSQL="delete from flight where id=?;";
    private static final String findFlightByOrigin="select *from flight where origin=?;";


    @Override
    public void save(FlightEntity entity) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");
            PreparedStatement query = conn.prepareStatement(ADD_FLIGHT_SQL);

            conn.setAutoCommit(false);
            query.setString(1, entity.getOrigin().name());
            query.setString(2, entity.getDestination().name());
            query.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            query.setInt(4, entity.getNumOfSeats());
            query.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LoggerService.logger.error("Flight Entity can not be save");
            }
        }

    }

    @Override
    public FlightEntity findById(long id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres"
            );
            PreparedStatement query = conn.prepareStatement(findFlightById);
            query.setLong(1, id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                return new FlightEntity(flightId, Cities.valueOf(origin), Cities.valueOf(destination), departureTime, numOfSeats);
            }
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<FlightEntity> findAll() {
        List<FlightEntity> flightEntities = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");

            PreparedStatement query = conn.prepareStatement(findAllFlightSQL);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntities.add(new FlightEntity(flightId, Cities.valueOf(origin), Cities.valueOf(destination), departureTime, numOfSeats));
            }

        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
        }
        return flightEntities;
    }

    @Override
    public void cancelFlight(long flightId) {
            Connection conn = null;
            try{
                conn=DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5433/postgres",
                        "postgres",
                        "postgres");
                PreparedStatement query=conn.prepareStatement(cancelFlightSQL);
                conn.setAutoCommit(false);
                query.setLong(1,flightId);
                query.executeUpdate();
                conn.commit();
            }catch(SQLException e){
                LoggerService.logger.error(e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    LoggerService.logger.error("Transaction rollback failed");
                }

            }
    }

    @Override
    public List<FlightEntity> findByOrigin(String origin) {
        List<FlightEntity> flightEntities = new ArrayList<>();
        Connection conn;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");

            PreparedStatement query = conn.prepareStatement(findFlightByOrigin);
            query.setString(1,origin);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String originCity= resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntities.add(new FlightEntity(flightId, Cities.valueOf(originCity), Cities.valueOf(destination), departureTime, numOfSeats));
            }

        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
        }
        return flightEntities;


    }

}
