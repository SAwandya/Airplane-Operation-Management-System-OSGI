package mobilepassengerapp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobilePassengerAppActivator implements BundleActivator {
    private MobilePassengerApp app;

    @Override
    public void start(BundleContext context) {
        app = new MobilePassengerApp(context);
        app.start();
    }

    @Override
    public void stop(BundleContext context) {
        if (app != null) {
            app.stop();
        }
    }
}