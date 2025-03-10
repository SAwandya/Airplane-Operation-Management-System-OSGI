package customsimmigrationsystem;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.FlightScheduleService;

import baggagehandlingservice.BaggageHandlingService;
import passengercheckinservice.PassengerCheckInService;
import passengerprofilingservice.PassengerProfilingService;
import securitycheckservice.SecurityCheckService;
import visaverificationservice.VisaVerificationService;

public class CustomsImmigrationActivator implements BundleActivator {
    // Field to hold the consumer instance
    private CustomsImmigrationSystem system;

    @Override
    public void start(BundleContext context) throws Exception {
        // Retrieve service references from the OSGi service registry
        ServiceReference<SecurityCheckService> securityRef = context.getServiceReference(SecurityCheckService.class);
        ServiceReference<PassengerCheckInService> checkInRef = context.getServiceReference(PassengerCheckInService.class);
        ServiceReference<PassengerProfilingService> profilingRef = context.getServiceReference(PassengerProfilingService.class);
        ServiceReference<VisaVerificationService> visaRef = context.getServiceReference(VisaVerificationService.class);
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        ServiceReference<BaggageHandlingService> baggageRef = context.getServiceReference(BaggageHandlingService.class);

        
        // Get the actual service instances (may be null if services are unavailable)
        SecurityCheckService securityService = (securityRef != null) ? context.getService(securityRef) : null;
        PassengerCheckInService checkInService = (checkInRef != null) ? context.getService(checkInRef) : null;
        PassengerProfilingService profilingService = (profilingRef != null) ? context.getService(profilingRef) : null;
        VisaVerificationService visaService = (visaRef != null) ? context.getService(visaRef) : null;
        FlightScheduleService flightService = (flightRef != null) ? context.getService(flightRef) : null;
        BaggageHandlingService baggageService = (baggageRef != null) ? context.getService(baggageRef) : null;

        
        // Initialize the consumer with the retrieved services
        system = new CustomsImmigrationSystem(securityService, checkInService, flightService, profilingService, visaService, baggageService);
        
        // Start the consumer, which begins the interactive terminal session
        system.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // Clean up by stopping the consumer if it exists
        if (system != null) {
            system.stop();
        }
    }
}
