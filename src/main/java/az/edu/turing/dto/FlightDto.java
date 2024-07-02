package az.edu.turing.dto;

import az.edu.turing.dao.entity.Cities;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightDto {
    private long id;
    private Cities origin;
    private Cities destination;
    private LocalDateTime departureTime;
    private int numberOfSeats;

    // Constructorlar, getterlər və setterlər
    public FlightDto() {
    }

    public FlightDto(long id, Cities origin, Cities destination, LocalDateTime departureTime, int numberOfSeats) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.numberOfSeats = numberOfSeats;
    }

    // Getter və setterlər
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cities getOrigin() {
        return origin;
    }

    public void setOrigin(Cities origin) {
        this.origin = origin;
    }

    public void setOrigin(String origin) {
        this.origin = Cities.valueOf(origin.toUpperCase());
    }

    public Cities getDestination() {
        return destination;
    }

    public void setDestination(Cities destination) {
        this.destination = destination;
    }

    public void setDestination(String destination) {
        this.destination = Cities.valueOf(destination.toUpperCase());
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightDto flightDto = (FlightDto) o;
        return id == flightDto.id && numberOfSeats == flightDto.numberOfSeats && origin == flightDto.origin && destination == flightDto.destination && Objects.equals(departureTime, flightDto.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, departureTime, numberOfSeats);
    }

    @Override
    public String toString() {
        return String.format("\n" + "{id=%d, origin='%s', destination='%s', departureTime=%s, numberOfSeats=%d}\n", id, origin, destination, departureTime, numberOfSeats);
    }


}
