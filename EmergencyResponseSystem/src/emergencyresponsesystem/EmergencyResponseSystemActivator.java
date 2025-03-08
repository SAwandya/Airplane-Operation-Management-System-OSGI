package emergencyresponsesystem;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EmergencyResponseSystemActivator implements BundleActivator {
    private EmergencyResponseSystem system;

    @Override
    public void start(BundleContext context) throws Exception {
        system = new EmergencyResponseSystem(context);
        system.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (system != null) {
            system.stop();
        }
    }
}
