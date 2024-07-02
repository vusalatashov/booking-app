package az.edu.turing.service;

import az.edu.turing.dto.CriteriaDto;
import az.edu.turing.dto.FlightDto;
import az.edu.turing.dao.entity.Cities;

import java.util.List;

public interface FlightService {
    void saveFlight(FlightDto flightDto);

    void cancelFlight(long flightId);

    List<FlightDto> findByOrigin(String origin);

    List<FlightDto> getFlightsByCriteria(CriteriaDto criteria);

    List<FlightDto> getAllFlights();

    FlightDto getFlightById(long id);

    List<FlightDto> getNext24HoursFlights(Cities origin);
}
