package baggagetrackingsystem;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaggageTrackingSystemActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(BaggageTrackingSystemActivator.class);
    private BaggageTrackingSystem trackingSystem;

    @Override
    public void start(BundleContext context) {
        trackingSystem = new BaggageTrackingSystem(context);
        trackingSystem.start();
//        logger.info("BaggageTrackingSystem started");
    }

    @Override
    public void stop(BundleContext context) {
        trackingSystem = null;
//        logger.info("BaggageTrackingSystem stopped");
    }
}
