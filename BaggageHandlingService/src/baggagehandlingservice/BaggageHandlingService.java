package baggagehandlingservice;

import java.util.List;

public interface BaggageHandlingService {
    String getBaggageStatusByTag(String tagNumber);
    String getBaggageStatusByPassenger(String passengerName, String flightNumber);
    String getBaggageStatus(String passengerId, String flightNumber);
}
