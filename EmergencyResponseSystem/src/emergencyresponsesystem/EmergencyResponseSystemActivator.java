package emergencyresponsesystem;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmergencyResponseSystemActivator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger(EmergencyResponseSystemActivator.class);
    private EmergencyResponseSystem system;

    @Override
    public void start(BundleContext context) {
        system = new EmergencyResponseSystem(context);
        system.start();
        logger.info("EmergencyResponseSystem started");
    }

    @Override
    public void stop(BundleContext context) {
        system = null;
        logger.info("EmergencyResponseSystem stopped");
    }
}
