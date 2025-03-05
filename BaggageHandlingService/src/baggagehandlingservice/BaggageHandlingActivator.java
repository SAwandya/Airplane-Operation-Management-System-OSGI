package baggagehandlingservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaggageHandlingActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(BaggageHandlingActivator.class);
    private ServiceRegistration<BaggageHandlingService> registration;

    @Override
    public void start(BundleContext context) {
        BaggageHandlingService service = new BaggageHandlingServiceImpl();
        registration = context.registerService(BaggageHandlingService.class, service, null);
//        logger.info("BaggageHandlingService registered");
        System.out.println("Ahunadoooooooooooooooooooooooooooooooooooooooooooo");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
//            logger.info("BaggageHandlingService unregistered");
        }
    }
}