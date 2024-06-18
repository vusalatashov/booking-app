package az.edu.turing.dao.entity;

import java.util.List;
import java.util.Objects;

public class BookingEntity {
    public static long MAX_ID=0;
    private long id;
    private long flight_id;
    private List<String> passengerNames;

    public BookingEntity() {

    }

    public BookingEntity(long id, long flight_id, List<String> passengerNames) {
        this.id = id;
        this.flight_id = flight_id;
        this.passengerNames = passengerNames;
    }

    public BookingEntity(long flight_id, List<String> passengerNames) {
        this.id=++MAX_ID;
        this.flight_id = flight_id;
        this.passengerNames = passengerNames;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id=id;
    }

    public long getFlight_id(){
        return flight_id;
    }

    public void setFlight_id(long flight_id){
        this.flight_id=flight_id;
    }

    public List<String> getPassengerNames(){
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames){
        this.passengerNames=passengerNames;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return id == that.id && flight_id == that.flight_id && Objects.equals(passengerNames, that.passengerNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flight_id, passengerNames);
    }

    @Override
    public String toString() {
        return String.format("\n"+"{id=%d, flightId='%s', passengerNames=%s}\n", id, flight_id, passengerNames);
    }
}
