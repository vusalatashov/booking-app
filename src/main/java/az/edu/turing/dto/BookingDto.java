package az.edu.turing.dto;

import java.util.List;
import java.util.Objects;

public class BookingDto {
    public static long MAX_ID=0;
    private long id;
    private long flightid;
    private List<String> passengerNames;

    public BookingDto() {

    }

    public BookingDto(long flightid, List<String> passengerNames) {
        this.id=++MAX_ID;
        this.flightid = flightid;
        this.passengerNames = passengerNames;
    }

    public BookingDto(long id, long flightid, List<String> passengerNames) {
        this.id = id;
        this.flightid = flightid;
        this.passengerNames = passengerNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFlightid() {
        return flightid;
    }

    public void setFlightid(long flightid) {
        this.flightid = flightid;
    }

    public List<String> getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames) {
        this.passengerNames = passengerNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return id == that.id && flightid == that.flightid && Objects.equals(passengerNames, that.passengerNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightid, passengerNames);
    }

    @Override
    public String toString() {
        return String.format("\n"+"{id=%d, flightId='%s', passengerNames=%s}", id, flightid, passengerNames);
    }

}
