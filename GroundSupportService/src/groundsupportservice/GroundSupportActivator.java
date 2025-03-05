package groundsupportservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroundSupportActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(GroundSupportActivator.class);
    private ServiceRegistration<GroundSupportService> registration;

    @Override
    public void start(BundleContext context) {
        GroundSupportService service = new GroundSupportServiceImpl();
        registration = context.registerService(GroundSupportService.class, service, null);
//        logger.info("GroundSupportService registered");
        System.out.println("Ground support activator start..");
        
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
//            logger.info("GroundSupportService unregistered");
        }
    }
}
