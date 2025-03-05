package mobilepassengerapp;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.FlightScheduleService;

import baggagehandlingservice.BaggageHandlingService;
import securitycheckservice.SecurityCheckService;

public class MobilePassengerApp {
//    private static final Logger logger = LoggerFactory.getLogger(MobilePassengerApp.class);
    private final BundleContext context;
    private FlightScheduleService flightService;
    private BaggageHandlingService baggageService;
    private SecurityCheckService securityService;

    public MobilePassengerApp(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        }

        ServiceReference<BaggageHandlingService> baggageRef = context.getServiceReference(BaggageHandlingService.class);
        if (baggageRef != null) {
            baggageService = context.getService(baggageRef);
        }

        ServiceReference<SecurityCheckService> securityRef = context.getServiceReference(SecurityCheckService.class);
        if (securityRef != null) {
            securityService = context.getService(securityRef);
        }

        if (flightService != null && baggageService != null && securityService != null) {
            showPassengerInfo("AA123", "BG123", "P123");
        } else {
//            logger.error("Required services missing for MobilePassengerApp");
        }
    }

    private void showPassengerInfo(String flightNumber, String baggageId, String passengerId) {
        if (flightService != null && baggageService != null && securityService != null) {
            String flightStatus = flightService.getFlightByNumber(flightNumber).getStatus();
            String baggageStatus = baggageService.getBaggageStatus(baggageId);
            String securityStatus = securityService.getScreeningStatus(passengerId);
//            logger.info("Flight {}: {}. Baggage {}: {}. Security for {}: {}", flightNumber, flightStatus, baggageId, baggageStatus, passengerId, securityStatus);
        } else {
//            logger.warn("Cannot show info for flight {}, baggage {}, passenger {}: services unavailable", flightNumber, baggageId, passengerId);
        }
    }
}
