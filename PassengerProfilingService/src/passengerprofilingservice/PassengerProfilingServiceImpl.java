package passengerprofilingservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PassengerProfilingServiceImpl implements PassengerProfilingService {
    private final Map<String, Integer> riskScores = new HashMap<>();
    private final Map<String, String> travelHistories = new HashMap<>();
    private final Random random = new Random();

    public PassengerProfilingServiceImpl() {
        riskScores.put("P123", 20);
        riskScores.put("P456", 85);
        travelHistories.put("P123", "Last trips: JFK-LAX, LAX-ORD");
        travelHistories.put("P456", "Last trips: SYD-DXB, DXB-LHR (flagged regions)");
    }

    @Override
    public int getRiskScore(String passengerId) {
        return riskScores.getOrDefault(passengerId, random.nextInt(101));
    }

    @Override
    public String getTravelHistorySummary(String passengerId) {
        return travelHistories.getOrDefault(passengerId, "No travel history available");
    }

    @Override
    public String predictRiskLevel(String passengerId, String flightNumber, String screeningStatus) {
        int score = getRiskScore(passengerId);
        StringBuilder prediction = new StringBuilder();
        if (score < 30 && "Cleared".equalsIgnoreCase(screeningStatus)) {
            prediction.append("Low Risk");
        } else if (score < 60) {
            prediction.append("Moderate Risk");
        } else {
            prediction.append("High Risk");
        }
        if (flightNumber.contains("FL456") && score > 50) {
            prediction.append(" - Flagged due to flight route");
        }
        return prediction.toString();
    }
}
