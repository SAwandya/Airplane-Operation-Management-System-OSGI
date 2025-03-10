package passengerprofilingservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PassengerProfilingActivator implements BundleActivator {

	private static BundleContext context;
    private ServiceRegistration<PassengerProfilingService> registration;


	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		PassengerProfilingActivator.context = bundleContext;
		PassengerProfilingService service = new PassengerProfilingServiceImpl();
	    registration = context.registerService(PassengerProfilingService.class, service, null);
		System.out.println("Passenger profuling service strat..");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		PassengerProfilingActivator.context = null;
	}

}
