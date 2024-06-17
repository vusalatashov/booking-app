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
    public static final String ADD_FLIGHT_SQL = "insert into flight(origin,destination,departure_time,free_seats)\n" +
            "values (?,?,?,?);";
    public static final String findFlightById="select *from flight where id=?;";
    public static final String findAllFlightSQL="select *from flight;";


    @Override
    public void save(FlightEntity entity) {
        Connection conn = null;
        try{ conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/postgres",
                "postgres",
                "postgres");
             PreparedStatement query = conn.prepareStatement(ADD_FLIGHT_SQL);

            conn.setAutoCommit(false);
          query.setString(1,entity.getOrigin().name());
          query.setString(2,entity.getDestination().name());
          query.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
          query.setInt(4,entity.getNumOfSeats());
          query.executeUpdate();
          conn.commit();
        }catch(SQLException e){
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
        FlightEntity flightEntity = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres"
            );
            PreparedStatement query = conn.prepareStatement(findFlightById);
            conn.setAutoCommit(false);

            query.setLong(1, id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntity = new FlightEntity(flightId, Cities.valueOf(origin), Cities.valueOf(destination), departureTime, numOfSeats);
            }
            conn.commit();
        } catch (SQLException e) {
            LoggerService.logger.error(e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LoggerService.logger.error("Transaction rollback failed", ex);
                }
            }
        }
        return flightEntity;
    }

    @Override
    public List<FlightEntity> findAll() {
        List<FlightEntity> flightEntities =new ArrayList<>();
        Connection conn = null;
        try{
            conn=DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5433/postgres",
                    "postgres",
                    "postgres");

            PreparedStatement query=conn.prepareStatement(findAllFlightSQL);
            conn.setAutoCommit(false);
            ResultSet resultSet=query.executeQuery();
            while(resultSet.next()){
                long flightId = resultSet.getLong("id");
                String origin = resultSet.getString("origin");
                String destination = resultSet.getString("destination");
                LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
                int numOfSeats = resultSet.getInt("free_seats");
                flightEntities.add(new FlightEntity(flightId, Cities.valueOf(origin), Cities.valueOf(destination), departureTime, numOfSeats));
            }

        }catch(SQLException e){
            LoggerService.logger.error(e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LoggerService.logger.error("Transaction rollback failed", ex);
            }
        }
        return flightEntities;
    }

    @Override
    public void cancelFlight(long flightId) {

    }

    @Override
    public List<FlightEntity> findByOrigin(String origin) {
        return null;
    }

    @Override
    public void update(FlightEntity entity) {

    }
}
