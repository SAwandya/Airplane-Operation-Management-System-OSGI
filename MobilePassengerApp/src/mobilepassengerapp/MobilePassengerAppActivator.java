package mobilepassengerapp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobilePassengerAppActivator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger(MobilePassengerAppActivator.class);
    private MobilePassengerApp app;

    @Override
    public void start(BundleContext context) {
        app = new MobilePassengerApp(context);
        app.start();
        logger.info("MobilePassengerApp started");
    }

    @Override
    public void stop(BundleContext context) {
        app = null;
        logger.info("MobilePassengerApp stopped");
    }
}
