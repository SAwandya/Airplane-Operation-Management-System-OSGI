package weatherairtrafficservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeatherAirTrafficServiceImpl implements WeatherAirTrafficService {
   // private static final Logger logger = LoggerFactory.getLogger(WeatherAirTrafficServiceImpl.class);
    private final Random random = new Random();
    private final Map<String, String> flightPathAdjustments = new HashMap<>();

    @Override
    public String getWeatherReport() {
        String[] conditions = {"Clear", "Cloudy", "Rainy", "Stormy", "Foggy"};
        String condition = conditions[random.nextInt(conditions.length)];
      //  logger.info("Generated weather report: {}", condition);
        return condition;
    }

    @Override
    public String getFlightPathAdjustment(String flightNumber) {
        if (flightNumber == null) {
           // logger.warn("Null flight number requested for path adjustment");
            return "Unknown";
        }
        if (!flightPathAdjustments.containsKey(flightNumber)) {
            flightPathAdjustments.put(flightNumber, "No adjustment");
        }
        simulatePathAdjustment(flightNumber);
        return flightPathAdjustments.get(flightNumber);
    }

    private void simulatePathAdjustment(String flightNumber) {
        if (random.nextInt(10) < 2) { // 20% chance
            String[] adjustments = {"Rerouted North", "Rerouted South", "Altitude Change", "Speed Adjustment"};
            String newAdjustment = adjustments[random.nextInt(adjustments.length)];
            flightPathAdjustments.put(flightNumber, newAdjustment);
         //   logger.info("Flight {} path adjusted: {}", flightNumber, newAdjustment);
        }
    }
}
