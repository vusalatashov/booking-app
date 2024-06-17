package az.edu.turing;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dao.entity.FlightEntity;
import az.edu.turing.dao.impl.FlightPostgresDao;
import az.edu.turing.log.Log;
import az.edu.turing.service.LoggerService;

import java.time.LocalDateTime;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        LoggerService.logger.info("This is an info message");
        LoggerService.logger.warn("This is a warning message");
        LoggerService.logger.error("This is an error message");

        FlightDao flightDao=new FlightPostgresDao();
        flightDao.save(new FlightEntity(Cities.MADRID,Cities.AMSTERDAM, LocalDateTime.now(),100));

        FlightEntity flight = flightDao.findById(1L);
        if (flight != null) {
            LoggerService.logger.info("Flight found: " + flight.toString());
        } else {
            LoggerService.logger.warn("No flight found with the given ID");
        }

        List<FlightEntity> flights=flightDao.findAll();
        LoggerService.logger.info("Show all flights:");
        for(FlightEntity f:flights){
            LoggerService.logger.info(f.toString());
        }
    }
}
