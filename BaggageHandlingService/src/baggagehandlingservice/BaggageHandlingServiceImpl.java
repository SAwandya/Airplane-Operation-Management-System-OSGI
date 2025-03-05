package baggagehandlingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BaggageHandlingServiceImpl implements BaggageHandlingService {
//    private static final Logger logger = LoggerFactory.getLogger(BaggageHandlingServiceImpl.class);
    private final ConcurrentHashMap<String, String> baggageStatus = new ConcurrentHashMap<>();

    public BaggageHandlingServiceImpl() {
        baggageStatus.put("BG123", "Checked In");
        baggageStatus.put("BG456", "In Transit");
        baggageStatus.put("BG789", "Delivered");
//        logger.info("BaggageHandlingService initialized with {} baggage entries", baggageStatus.size());
    }

    @Override
    public String getBaggageStatus(String baggageId) {
        return baggageStatus.getOrDefault(baggageId, "Unknown");
    }

    @Override
    public void updateBaggageStatus(String baggageId, String status) {
        if (baggageId == null || status == null) {
//            logger.warn("Invalid baggage update: baggageId={}, status={}", baggageId, status);
            return;
        }
        baggageStatus.put(baggageId, status);
//        logger.info("Baggage {} status updated to {}", baggageId, status);
    }
}
