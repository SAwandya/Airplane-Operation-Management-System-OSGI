package passengercheckinservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerCheckInActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(PassengerCheckInActivator.class);
    private ServiceRegistration<PassengerCheckInService> registration;

    @Override
    public void start(BundleContext context) {
        PassengerCheckInService service = new PassengerCheckInServiceImpl();
        registration = context.registerService(PassengerCheckInService.class, service, null);
//        logger.info("PassengerCheckInService registered");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
//            logger.info("PassengerCheckInService unregistered");
        }
    }
}
