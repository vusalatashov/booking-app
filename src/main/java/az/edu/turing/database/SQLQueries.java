package az.edu.turing.database;

public class SQLQueries {
    // Booking queries
    public static final String CREATE_BOOKING = "INSERT INTO booking(flight_id) VALUES(?);";
    public static final String ADD_PASSENGER = "INSERT INTO passenger(full_name) VALUES(?);";
    public static final String ADD_BOOKING_PASSENGER = "INSERT INTO booking_passenger(booking_id, passenger_id) VALUES(?,?);";
    public static final String GET_BOOKING_BY_ID = "SELECT booking.id, booking.flight_id, string_agg(full_name, ',') as full_names FROM booking JOIN booking_passenger bp ON booking.id = bp.booking_id JOIN passenger p ON p.id = bp.passenger_id WHERE booking.id=? GROUP BY booking.id;";
    public static final String GET_ALL_BOOKING = "SELECT booking.id, booking.flight_id, string_agg(full_name, ',') as full_names FROM booking JOIN booking_passenger bp ON booking.id = bp.booking_id JOIN passenger p ON p.id = bp.passenger_id GROUP BY booking.id;";
    public static final String GET_BOOKING_BY_FULLNAME = "SELECT booking.id, booking.flight_id, string_agg(full_name, ',') as full_names FROM booking JOIN booking_passenger bp ON booking.id = bp.booking_id JOIN passenger p ON p.id = bp.passenger_id WHERE booking.id IN (SELECT booking.id FROM booking JOIN booking_passenger b ON booking.id = b.booking_id JOIN passenger p2 ON p2.id = b.passenger_id WHERE p2.full_name = ?) GROUP BY booking.id;";
    public static final String DELETE_BOOKING_PASSENGER = "DELETE FROM booking_passenger WHERE booking_id=?;";
    public static final String DELETE_BOOKING = "DELETE FROM booking WHERE id=?;";
    public static final String DECREASE_SEAT_COUNT = "UPDATE flight SET free_seats = free_seats - ? WHERE id = ?;";
    public static final String INCREASE_SEAT_COUNT = "UPDATE flight SET free_seats = free_seats + ? WHERE id = ?;";

    // Flight queries
    public static final String ADD_FLIGHT_SQL = "INSERT INTO flight (origin, destination, departure_time, free_seats) VALUES (?, ?, ?, ?);";
    public static final String FIND_FLIGHT_BY_ID_SQL = "SELECT * FROM flight WHERE id = ?;";
    public static final String FIND_ALL_FLIGHTS_SQL = "SELECT * FROM flight;";
    public static final String CANCEL_FLIGHT_SQL = "DELETE FROM flight WHERE id = ?;";
    public static final String FIND_FLIGHT_BY_ORIGIN_SQL = "SELECT * FROM flight WHERE origin = ?;";
    public static final String DELETE_BOOKING_PASSENGER_BY_FLIGHT_ID = "DELETE FROM booking_passenger WHERE booking_id IN (SELECT id FROM booking WHERE flight_id = ?)";
    public static final String DELETE_BOOKINGS_BY_FLIGHT_ID = "DELETE FROM booking WHERE flight_id = ?";
}
