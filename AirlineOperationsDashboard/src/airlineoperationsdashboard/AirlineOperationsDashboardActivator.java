package airlineoperationsdashboard;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirlineOperationsDashboardActivator implements BundleActivator {
//    private static final Logger logger = LoggerFactory.getLogger(AirlineOperationsDashboardActivator.class);
    private AirlineOperationsDashboard dashboard;

    @Override
    public void start(BundleContext context) {
        dashboard = new AirlineOperationsDashboard(context);
        dashboard.start();
//        logger.info("AirlineOperationsDashboard started");
    }

    @Override
    public void stop(BundleContext context) {
        dashboard = null;
//        logger.info("AirlineOperationsDashboard stopped");
    }
}
