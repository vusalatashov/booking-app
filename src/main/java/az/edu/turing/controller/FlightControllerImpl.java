package az.edu.turing.controller;

import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dto.CriteriaDto;
import az.edu.turing.dto.FlightDto;
import az.edu.turing.service.FlightService;

import java.util.List;

public class FlightControllerImpl implements FlightController{
      private final FlightService flightService;

      public FlightControllerImpl(FlightService flightService) {
          this.flightService = flightService;
      }

    @Override
    public void cancelFlight(long flightId) {
          flightService.cancelFlight(flightId);
    }

    @Override
    public void saveFlight(FlightDto flightDto) {
        flightService.saveFlight(flightDto);
    }

    @Override
    public List<FlightDto> findByOrigin(String origin) {
        return flightService.findByOrigin(origin);
    }

    @Override
    public List<FlightDto> getFlightsByCriteria(CriteriaDto criteria) {
        return flightService.getFlightsByCriteria(criteria);
    }

    @Override
    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    @Override
    public FlightDto getFlightById(long flightId) {
        return flightService.getFlightById(flightId);
    }

    @Override
    public List<FlightDto> getNext24HoursFlights(Cities origin) {
        return flightService.getNext24HoursFlights(origin);
    }



}
