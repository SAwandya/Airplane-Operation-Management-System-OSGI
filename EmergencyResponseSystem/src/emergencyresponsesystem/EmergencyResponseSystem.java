package emergencyresponsesystem;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.FlightScheduleService;

import weatherairtrafficservice.WeatherAirTrafficService;

public class EmergencyResponseSystem {
    private static final Logger logger = LoggerFactory.getLogger(EmergencyResponseSystem.class);
    private final BundleContext context;
    private FlightScheduleService flightService;
    private WeatherAirTrafficService weatherService;

    public EmergencyResponseSystem(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        }

        ServiceReference<WeatherAirTrafficService> weatherRef = context.getServiceReference(WeatherAirTrafficService.class);
        if (weatherRef != null) {
            weatherService = context.getService(weatherRef);
        }

        if (flightService != null && weatherService != null) {
            assessSituation("AA123");
        } else {
            logger.error("Required services missing for EmergencyResponseSystem");
        }
    }

    private void assessSituation(String flightNumber) {
        if (flightService != null && weatherService != null) {
            String flightStatus = flightService.getFlightByNumber(flightNumber).getStatus();
            String weatherReport = weatherService.getWeatherReport();
            logger.info("Flight {} status: {}. Weather: {}", flightNumber, flightStatus, weatherReport);
        } else {
            logger.warn("Cannot assess situation for {}: services unavailable", flightNumber);
        }
    }
}
