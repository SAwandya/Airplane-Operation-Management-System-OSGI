package baggagetrackingsystem;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baggagehandlingservice.BaggageHandlingService;

public class BaggageTrackingSystem {
    private static final Logger logger = LoggerFactory.getLogger(BaggageTrackingSystem.class);
    private final BundleContext context;
    private BaggageHandlingService baggageService;

    public BaggageTrackingSystem(BundleContext context) {
        this.context = context;
    }

    public void start() {
        ServiceReference<BaggageHandlingService> ref = context.getServiceReference(BaggageHandlingService.class);
        if (ref != null) {
            baggageService = context.getService(ref);
            if (baggageService != null) {
                trackBaggage("BG123");
            } else {
                logger.error("BaggageHandlingService not available");
            }
        } else {
            logger.error("No BaggageHandlingService reference found");
        }
    }

    private void trackBaggage(String baggageId) {
        if (baggageService != null) {
            String status = baggageService.getBaggageStatus(baggageId);
            logger.info("Baggage {} status: {}", baggageId, status);
        } else {
            logger.warn("Cannot track baggage {}: service unavailable", baggageId);
        }
    }
}