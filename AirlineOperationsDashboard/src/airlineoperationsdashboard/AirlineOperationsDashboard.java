package airlineoperationsdashboard;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.FlightScheduleService;

import groundsupportservice.GroundSupportService;
import weatherairtrafficservice.WeatherAirTrafficService;

public class AirlineOperationsDashboard {
//    private static final Logger logger = LoggerFactory.getLogger(AirlineOperationsDashboard.class);
    private final BundleContext context;
    private FlightScheduleService flightService;
    private GroundSupportService groundService;
    private WeatherAirTrafficService weatherService;

    public AirlineOperationsDashboard(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        }

        ServiceReference<GroundSupportService> groundRef = context.getServiceReference(GroundSupportService.class);
        if (groundRef != null) {
            groundService = context.getService(groundRef);
        }

        ServiceReference<WeatherAirTrafficService> weatherRef = context.getServiceReference(WeatherAirTrafficService.class);
        if (weatherRef != null) {
            weatherService = context.getService(weatherRef);
        }

        if (flightService != null && groundService != null && weatherService != null) {
            displayDashboard("AA123");
        } else {
//            logger.error("Required services missing for AirlineOperationsDashboard");
        }
    }

    private void displayDashboard(String flightNumber) {
        if (flightService != null && groundService != null && weatherService != null) {
            String flightStatus = flightService.getFlightByNumber(flightNumber).getStatus();
            String groundStatus = groundService.getGroundSupportStatus(flightNumber);
            String weatherReport = weatherService.getWeatherReport();
//            logger.info("Flight {}: Status={}, Ground Support={}, Weather={}", flightNumber, flightStatus, groundStatus, weatherReport);
        } else {
//            logger.warn("Cannot display dashboard for {}: services unavailable", flightNumber);
        }
    }
}
