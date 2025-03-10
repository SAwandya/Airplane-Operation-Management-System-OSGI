package visaverificationservice;

import java.util.HashMap;
import java.util.Map;

public class VisaVerificationServiceImpl implements VisaVerificationService {
    private final Map<String, String> visaData = new HashMap<>(); // passengerId -> visa status
    private final Map<String, Boolean> visaRequirements = new HashMap<>(); // destination -> visa required

    public VisaVerificationServiceImpl() {
        // Sample data
        visaData.put("P123", "Valid until 2025-12-31");
        visaData.put("P456", "Expired");
        visaRequirements.put("JFK", false); // No visa for US domestic
        visaRequirements.put("LHR", true);  // Visa required for UK
        visaRequirements.put("ORD", false);
        visaRequirements.put("MIA", false);
    }

    @Override
    public String checkVisaStatus(String passengerId, String destination) {
        if (visaRequirements.getOrDefault(destination, true)) {
            return visaData.getOrDefault(passengerId, "No visa on record");
        }
        return "No visa required for " + destination;
    }

    @Override
    public boolean isVisaRequired(String destination) {
        return visaRequirements.getOrDefault(destination, true);
    }
}
