package az.edu.turing.controller;


import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dto.CriteriaDto;
import az.edu.turing.dto.FlightDto;

import java.util.List;

public interface FlightController {
    void saveFlight(FlightDto flightDto);

    void cancelFlight(long flightId);

    List<FlightDto> findByOrigin(String origin);

    List<FlightDto> getFlightsByCriteria(CriteriaDto criteria);

    List<FlightDto> getAllFlights();

    FlightDto getFlightById(long flightId);

    List<FlightDto> getNext24HoursFlights(Cities origin);

}
