package securitycheckservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityCheckActivator implements BundleActivator {
    private static final Logger logger = LoggerFactory.getLogger(SecurityCheckActivator.class);
    private ServiceRegistration<SecurityCheckService> registration;

    @Override
    public void start(BundleContext context) {
        SecurityCheckService service = new SecurityCheckServiceImpl();
        registration = context.registerService(SecurityCheckService.class, service, null);
        logger.info("SecurityCheckService registered");
    }

    @Override
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
            logger.info("SecurityCheckService unregistered");
        }
    }
}
