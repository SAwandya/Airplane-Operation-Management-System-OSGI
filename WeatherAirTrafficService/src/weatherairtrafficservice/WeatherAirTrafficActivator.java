package weatherairtrafficservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherAirTrafficActivator implements BundleActivator {
   // private static final Logger logger = LoggerFactory.getLogger(WeatherAirTrafficActivator.class);
    private ServiceRegistration<WeatherAirTrafficService> registration;

    @Override
    public void start(BundleContext context) {
      //  WeatherAirTrafficService service = new WeatherAirTrafficServiceImpl();
     //   registration = context.registerService(WeatherAirTrafficService.class, service, null);
       // logger.info("WeatherAirTrafficService registered");
    	System.out.println("Weather Success");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
         //   logger.info("WeatherAirTrafficService unregistered");
        }
    }
}