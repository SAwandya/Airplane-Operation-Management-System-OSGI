package customsimmigrationsystem;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomsImmigrationSystemActivator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger(CustomsImmigrationSystemActivator.class);
    private CustomsImmigrationSystem system;

    @Override
    public void start(BundleContext context) {
        system = new CustomsImmigrationSystem(context);
        system.start();
        logger.info("CustomsImmigrationSystem started");
    }

    @Override
    public void stop(BundleContext context) {
        system = null;
        logger.info("CustomsImmigrationSystem stopped");
    }
}
