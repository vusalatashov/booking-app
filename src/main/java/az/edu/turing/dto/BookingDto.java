package az.edu.turing.dto;

import java.util.List;

public class BookingDto {
    private long id;
    private long flightId;
    private List<String> passengerNames;

    public BookingDto() {
        // Default constructor
    }

    public BookingDto(long flightId, List<String> passengerNames) {
        this.flightId = flightId;
        this.passengerNames = passengerNames;
    }

    public BookingDto(long id, long flightId, List<String> passengerNames) {
        this.id = id;
        this.flightId = flightId;
        this.passengerNames = passengerNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public List<String> getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames) {
        this.passengerNames = passengerNames;
    }
}
