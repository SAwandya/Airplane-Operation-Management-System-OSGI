package customsimmigrationsystem;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import passengercheckinservice.PassengerCheckInService;
import securitycheckservice.SecurityCheckService;

public class CustomsImmigrationSystem {
    private static final Logger logger = LoggerFactory.getLogger(CustomsImmigrationSystem.class);
    private final BundleContext context;
    private SecurityCheckService securityService;
    private PassengerCheckInService checkInService;

    public CustomsImmigrationSystem(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<SecurityCheckService> securityRef = context.getServiceReference(SecurityCheckService.class);
        if (securityRef != null) {
            securityService = context.getService(securityRef);
        }

        ServiceReference<PassengerCheckInService> checkInRef = context.getServiceReference(PassengerCheckInService.class);
        if (checkInRef != null) {
            checkInService = context.getService(checkInRef);
        }

        if (securityService != null && checkInService != null) {
            trackClearance("P123", "AA123");
        } else {
            logger.error("Required services missing for CustomsImmigrationSystem");
        }
    }

    private void trackClearance(String passengerId, String flightNumber) {
        if (securityService != null && checkInService != null) {
            String securityStatus = securityService.getScreeningStatus(passengerId);
            boolean isCheckedIn = checkInService.isCheckedIn(passengerId, flightNumber);
            logger.info("Passenger {} security status: {}. Checked in: {}", passengerId, securityStatus, isCheckedIn);
        } else {
            logger.warn("Cannot track clearance for passenger {}, flight {}: services unavailable", passengerId, flightNumber);
        }
    }
}
