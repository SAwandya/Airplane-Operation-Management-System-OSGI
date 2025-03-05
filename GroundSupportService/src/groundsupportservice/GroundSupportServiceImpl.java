package groundsupportservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class GroundSupportServiceImpl implements GroundSupportService {
//    private static final Logger logger = LoggerFactory.getLogger(GroundSupportServiceImpl.class);
    private final ConcurrentHashMap<String, String> flightSupportStatus = new ConcurrentHashMap<>();

    public GroundSupportServiceImpl() {
        flightSupportStatus.put("AA123", "Refueling");
        flightSupportStatus.put("UA456", "Catering");
        flightSupportStatus.put("DL789", "Maintenance");
//        logger.info("GroundSupportService initialized with {} flights", flightSupportStatus.size());
    }

    @Override
    public String getGroundSupportStatus(String flightNumber) {
        return flightSupportStatus.getOrDefault(flightNumber, "Unknown");
    }

    @Override
    public void updateGroundSupportStatus(String flightNumber, String status) {
        if (flightNumber == null || status == null) {
//            logger.warn("Invalid ground support update: flightNumber={}, status={}", flightNumber, status);
            return;
        }
        flightSupportStatus.put(flightNumber, status);
//        logger.info("Flight {} ground support status updated to {}", flightNumber, status);
    }
}
