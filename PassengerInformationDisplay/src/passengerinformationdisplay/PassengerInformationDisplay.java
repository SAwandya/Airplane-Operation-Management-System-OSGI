package passengerinformationdisplay;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.Flight;
import com.osgi.flightsheduleservice.FlightScheduleListener;
import com.osgi.flightsheduleservice.FlightScheduleService;

public class PassengerInformationDisplay implements FlightScheduleListener {
    private static final Logger logger = LoggerFactory.getLogger(PassengerInformationDisplay.class);
    private final BundleContext context;
    private FlightScheduleService flightService;

    public PassengerInformationDisplay(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<FlightScheduleService> ref = context.getServiceReference(FlightScheduleService.class);
        if (ref != null) {
            flightService = context.getService(ref);
            if (flightService != null) {
                flightService.registerListener(this);
                displayFlightInfo();
            } else {
                logger.error("FlightScheduleService not available");
            }
        } else {
            logger.error("No FlightScheduleService reference found");
        }
    }

    public void stop() {
        if (flightService != null) {
            flightService.unregisterListener(this);
            flightService = null;
        }
    }

    private void displayFlightInfo() {
        if (flightService != null) {
            for (Flight flight : flightService.getAllFlights()) {
                logger.info("Flight {}: Status={}, Gate={}, ETA={}", flight.getFlightNumber(),
                        flight.getStatus(), flight.getGate(), flight.getEstimatedArrival());
            }
        } else {
            logger.warn("Cannot display flight info: service unavailable");
        }
    }

    @Override
    public void onStatusChanged(String flightNumber, String newStatus) {
        logger.info("Flight {} status updated to {}", flightNumber, newStatus);
        displayFlightInfo();
    }

    @Override
    public void onGateChanged(String flightNumber, String newGate) {
        logger.info("Flight {} gate updated to {}", flightNumber, newGate);
        displayFlightInfo();
    }

	@Override
	public void onFlightUpdated(Flight flight) {
		// TODO Auto-generated method stub
		
	}
}
