package baggagehandlingservice;

public interface BaggageHandlingService {
    String getBaggageStatus(String baggageId);
    void updateBaggageStatus(String baggageId, String status);
}
