package az.edu.turing.dao.entity;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BookingEntity {
    private static final AtomicLong MAX_ID = new AtomicLong(0);
    private long id;
    private long flightId;
    private List<String> passengerNames;

    public BookingEntity() {
        // Default constructor
    }

    public BookingEntity(long id, long flightId, List<String> passengerNames) {
        this.id = id;
        this.flightId = flightId;
        this.passengerNames = passengerNames;
    }

    public BookingEntity(long flightId, List<String> passengerNames) {
        this.id = MAX_ID.incrementAndGet();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return id == that.id && flightId == that.flightId && Objects.equals(passengerNames, that.passengerNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightId, passengerNames);
    }

    @Override
    public String toString() {
        return String.format("{id=%d, flightId=%d, passengerNames=%s}", id, flightId, passengerNames);
    }
}
