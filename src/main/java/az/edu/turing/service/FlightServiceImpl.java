package az.edu.turing.service;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.Cities;
import az.edu.turing.dao.entity.FlightEntity;
import az.edu.turing.dto.CriteriaDto;
import az.edu.turing.dto.FlightDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {
    private static final Logger logger = LoggerFactory.getLogger(FlightServiceImpl.class);
    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    @Override
    public void saveFlight(FlightDto flightDto) {
        try {
            FlightEntity flightEntity = new FlightEntity(flightDto.getOrigin(),
                    flightDto.getDestination(), flightDto.getDepartureTime(), flightDto.getNumberOfSeats());
            flightDao.save(flightEntity);

            if (flightEntity.getId() == 0) {
                logger.error("Flight failed, ID is zero");
            }

            flightDto.setId(flightEntity.getId());
        } catch (Exception e) {
            logger.error("Exception while saving flight", e);
        }
    }

    @Override
    public void cancelFlight(long flightId) {
        try {
            FlightEntity flightEntity = flightDao.findById(flightId);
            if (flightEntity == null) {
                logger.error("Flight not found with id: {}", flightId);
            } else {
                flightDao.cancelFlight(flightId);
            }
        } catch (Exception e) {
            logger.error("Exception while cancelling flight", e);
        }
    }

    @Override
    public List<FlightDto> findByOrigin(String origin) {
        try {
            return flightDao.findByOrigin(origin).stream()
                    .map(flight -> new FlightDto(flight.getId(), flight.getOrigin(),
                            flight.getDestination(), flight.getDepartureTime(), flight.getNumOfSeats()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Exception while finding flights by origin", e);
            return List.of();
        }
    }

    @Override
    public List<FlightDto> getFlightsByCriteria(CriteriaDto criteria) {
        try {
            List<FlightEntity> entities = flightDao.findAll();
            return entities.stream().filter(entity ->
                            entity.getDestination().equals(criteria.getDestination()) &&
                                    entity.getDepartureTime().equals(criteria.getTime()) &&
                                    entity.getNumOfSeats() >= criteria.getSeats())
                    .map(entity -> new FlightDto(entity.getId(), entity.getOrigin(),
                            entity.getDestination(), entity.getDepartureTime(), entity.getNumOfSeats()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Exception while getting flights by criteria", e);
            return List.of();
        }
    }

    @Override
    public List<FlightDto> getAllFlights() {
        try {
            return flightDao.findAll().stream()
                    .map(flight -> new FlightDto(flight.getId(), flight.getOrigin(),
                            flight.getDestination(), flight.getDepartureTime(), flight.getNumOfSeats()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Exception while getting all flights", e);
            return List.of();
        }
    }

    @Override
    public FlightDto getFlightById(long id) {
        try {
            FlightEntity flightEntity = flightDao.findById(id);
            if (flightEntity == null) {
                logger.error("Flight not found with id: {}", id);
                return null;
            }
            return new FlightDto(flightEntity.getId(), flightEntity.getOrigin(), flightEntity.getDestination(), flightEntity.getDepartureTime(), flightEntity.getNumOfSeats());
        } catch (Exception e) {
            logger.error("Exception while getting flight by id", e);
            return null;
        }
    }

    @Override
    public List<FlightDto> getNext24HoursFlights(Cities origin) {
        try {
            List<FlightEntity> entities = flightDao.findAll();
            return entities.stream().filter(entity ->
                            entity.getOrigin().equals(origin) &&
                                    entity.getDepartureTime().isAfter(LocalDateTime.now()) &&
                                    entity.getDepartureTime().isBefore(LocalDateTime.now().plusHours(24)))
                    .map(entity -> new FlightDto(entity.getId(), entity.getOrigin(),
                            entity.getDestination(), entity.getDepartureTime(), entity.getNumOfSeats()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Exception while getting next 24 hours flights", e);
            return List.of();
        }
    }
}
