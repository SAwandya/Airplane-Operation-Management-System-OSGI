package visaverificationservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class VisaVerificationActivator implements BundleActivator {

	private static BundleContext context;
    private ServiceRegistration<VisaVerificationService> registration;

	
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		VisaVerificationActivator.context = bundleContext;
		VisaVerificationService service = new VisaVerificationServiceImpl();
	    registration = context.registerService(VisaVerificationService.class, service, null);
		System.out.println("Visa verfication service start..");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		VisaVerificationActivator.context = null;
	}

}
