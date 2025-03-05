package passengerinformationdisplay;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerInformationDisplayActivator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger(PassengerInformationDisplayActivator.class);
    private PassengerInformationDisplay display;

    @Override
    public void start(BundleContext context) {
        display = new PassengerInformationDisplay(context);
        display.start();
        logger.info("PassengerInformationDisplay started");
    }

    @Override
    public void stop(BundleContext context) {
        if (display != null) {
            display.stop();
        }
        logger.info("PassengerInformationDisplay stopped");
    }
}
