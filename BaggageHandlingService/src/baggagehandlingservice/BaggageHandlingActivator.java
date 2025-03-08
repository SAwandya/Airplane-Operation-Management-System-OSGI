package baggagehandlingservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaggageHandlingActivator implements BundleActivator {
    private ServiceRegistration<BaggageHandlingService> registration;

    @Override
    public void start(BundleContext context) {
        BaggageHandlingService service = new BaggageHandlingServiceImpl();
        registration = context.registerService(BaggageHandlingService.class, service, null);
        System.out.println("BaggageHandlingService registered");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
        }
        System.out.println("BaggageHandlingService unregistered");
    }
}