package az.edu.turing.dao.impl;

import az.edu.turing.dao.FlightDao;
import az.edu.turing.dao.entity.FlightEntity;

import java.util.List;

public class FlightPostgresDao implements FlightDao {
    @Override
    public void save(FlightEntity entity) {

    }

    @Override
    public FlightEntity findById(long id) {
        return null;
    }

    @Override
    public List<FlightEntity> findAll() {
        return null;
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
