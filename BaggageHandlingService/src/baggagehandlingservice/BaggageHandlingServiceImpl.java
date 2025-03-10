package baggagehandlingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaggageHandlingServiceImpl implements BaggageHandlingService {
    private Map<String, String> baggageByTag = new HashMap<>();
    private Map<String, Map<String, String>> baggageByPassenger = new HashMap<>(); // passenger -> flight -> status

    private Map<String, Map<String, String>> baggageData = new HashMap<>();

    public BaggageHandlingServiceImpl() {
        // Sample data
        baggageByTag.put("TAG123", "Checked In");
        baggageByTag.put("TAG456", "In Transit");
        baggageByTag.put("TAG789", "Loaded");

        Map<String, String> johnDoeFlights = new HashMap<>();
        johnDoeFlights.put("FL123", "Checked In");
        baggageByPassenger.put("John Doe", johnDoeFlights);

        Map<String, String> janeSmithFlights = new HashMap<>();
        janeSmithFlights.put("FL456", "In Transit");
        baggageByPassenger.put("Jane Smith", janeSmithFlights);
        
     // Sample data: passengerId -> flightNumber -> status
        Map<String, String> johnFlights = new HashMap<>();
        johnFlights.put("FL123", "Checked In");
        baggageData.put("P001", johnFlights);

        Map<String, String> janeFlights = new HashMap<>();
        janeFlights.put("FL456", "In Transit");
        baggageData.put("P002", janeFlights);
    }
    
    @Override
    public String getBaggageStatus(String passengerId, String flightNumber) {
        Map<String, String> passengerFlights = baggageData.get(passengerId);
        if (passengerFlights != null) {
            return passengerFlights.getOrDefault(flightNumber, "No baggage found for this flight");
        }
        return "Passenger not found";
    }

    @Override
    public String getBaggageStatusByTag(String tagNumber) {
        return baggageByTag.getOrDefault(tagNumber, "Baggage not found");
    }

    @Override
    public String getBaggageStatusByPassenger(String passengerName, String flightNumber) {
        Map<String, String> passengerFlights = baggageByPassenger.get(passengerName);
        if (passengerFlights != null) {
            return passengerFlights.getOrDefault(flightNumber, "No baggage found for this flight");
        }
        return "Passenger not found";
    }
}
