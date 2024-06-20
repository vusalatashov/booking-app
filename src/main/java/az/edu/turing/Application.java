package az.edu.turing;

import az.edu.turing.dao.BookingDao;
import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.BookingEntity;
import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dao.entity.FlightEntity;
import az.edu.turing.dao.impl.BookingPostgresDao;
import az.edu.turing.dao.impl.FlightPostgresDao;
import az.edu.turing.log.Log;
import az.edu.turing.service.LoggerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        LoggerService.logger.info("This is an info message");
        LoggerService.logger.warn("This is a warning message");
        LoggerService.logger.error("This is an error message");

       /*  FlightDao flightDao=new FlightPostgresDao();
        flightDao.cancelFlight(3);
        System.out.println(flightDao.findAll());
        System.out.println(flightDao.findByOrigin("MADRID"));

        */

        BookingDao bookingDao=new BookingPostgresDao();
     //   bookingDao.findById(1);


        ArrayList<String> name=new ArrayList<>();
        name.add("Alex");
        name.add("Bob");
        BookingEntity bookingEntity=new BookingEntity(7,name);
      // bookingDao.save(bookingEntity);
       // System.out.println(bookingDao.findById(3));
        System.out.println(bookingDao.findAll());
       // bookingDao.cancelBooking(7);
        bookingDao.cancelBooking(2);

       /*  flightDao.save(new FlightEntity(Cities.MADRID,Cities.AMSTERDAM, LocalDateTime.now(),100));

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

        flightDao.cancelFlight(flight.getId());
        LoggerService.logger.info("Flight with ID " + flight.getId() + " has been cancelled");

        FlightEntity cancelledFlight=flightDao.findById(flight.getId());
        if(cancelledFlight==null){
            LoggerService.logger.info("Flight with ID " + flight.getId() + " has been cancelled");
        }
        else{
            LoggerService.logger.warn("Flight with ID " + cancelledFlight.getId() + "still exist in database");
        }

        String originCity = "MADRID";
        List<FlightEntity> flightsFromOrigin = flightDao.findByOrigin(originCity);
        LoggerService.logger.info("Showing all flights from " + originCity + ":");
        for (FlightEntity f : flightsFromOrigin) {
            LoggerService.logger.info(f.toString());
        }

        */
    }
}
