package passengercheckinservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class PassengerCheckInServiceImpl implements PassengerCheckInService {
//    private static final Logger logger = LoggerFactory.getLogger(PassengerCheckInServiceImpl.class);
    private final ConcurrentHashMap<String, String> passengerSeats = new ConcurrentHashMap<>();

    public PassengerCheckInServiceImpl() {
        // Sample data for testing
        passengerSeats.put("P123_FL123", "12A");
        passengerSeats.put("P456_FL456", "15B");
//        logger.info("PassengerCheckInService initialized");
    }
    
    @Override
    public boolean isCheckedIn(String passengerId, String flightNumber) {
        if (passengerId == null || flightNumber == null) {
//            logger.warn("Invalid check-in check: passengerId={}, flightNumber={}", passengerId, flightNumber);
            return false;
        }
        String key = passengerId + "_" + flightNumber;
        return passengerSeats.containsKey(key);
    }

    @Override
    public String getSeat(String passengerId, String flightNumber) {
        if (passengerId == null || flightNumber == null) {
//            logger.warn("Invalid seat request: passengerId={}, flightNumber={}", passengerId, flightNumber);
            return "Unknown";
        }
        String key = passengerId + "_" + flightNumber;
        return passengerSeats.getOrDefault(key, "Not Assigned");
    }

    @Override
    public void checkInPassenger(String passengerId, String flightNumber, String seat) {
        if (passengerId == null || flightNumber == null || seat == null) {
//            logger.warn("Invalid check-in: passengerId={}, flightNumber={}, seat={}", passengerId, flightNumber, seat);
            return;
        }
        String key = passengerId + "_" + flightNumber;
        passengerSeats.put(key, seat);
//        logger.info("Passenger {} checked in for flight {} with seat {}", passengerId, flightNumber, seat);
    }
    
    public String getPassengerDetails(String passengerId, String flightNumber) {
        if (isCheckedIn(passengerId, flightNumber)) {
            return "Passenger ID: " + passengerId + ", Flight: " + flightNumber + ", Seat: " + getSeat(passengerId, flightNumber);
        }
        return "Passenger ID: " + passengerId + ", Flight: " + flightNumber + " - Not checked in";
    }
}
