package passengerprofilingservice;

public interface PassengerProfilingService {
    int getRiskScore(String passengerId);
    String getTravelHistorySummary(String passengerId);
    String predictRiskLevel(String passengerId, String flightNumber, String screeningStatus); // New AI-driven method
}
