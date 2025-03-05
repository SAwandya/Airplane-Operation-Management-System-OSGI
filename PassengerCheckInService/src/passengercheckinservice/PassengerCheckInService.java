package passengercheckinservice;

public interface PassengerCheckInService {
    boolean isCheckedIn(String passengerId, String flightNumber);
    String getSeat(String passengerId, String flightNumber);
    void checkInPassenger(String passengerId, String flightNumber, String seat);
}
