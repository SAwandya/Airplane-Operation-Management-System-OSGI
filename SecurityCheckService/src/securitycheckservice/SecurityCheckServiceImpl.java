package securitycheckservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SecurityCheckServiceImpl implements SecurityCheckService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityCheckServiceImpl.class);
    private final ConcurrentHashMap<String, String> screeningStatus = new ConcurrentHashMap<>();
    private final AtomicInteger queueLength = new AtomicInteger(0);

    public SecurityCheckServiceImpl() {
        screeningStatus.put("P123", "Waiting");
        screeningStatus.put("P456", "Cleared");
        queueLength.set(5);
        logger.info("SecurityCheckService initialized");
    }

    @Override
    public String getScreeningStatus(String passengerId) {
        return screeningStatus.getOrDefault(passengerId, "Unknown");
    }

    @Override
    public int getQueueLength() {
        return queueLength.get();
    }

    @Override
    public void updateScreeningStatus(String passengerId, String status) {
        if (passengerId == null || status == null) {
            logger.warn("Invalid screening update: passengerId={}, status={}", passengerId, status);
            return;
        }
        screeningStatus.put(passengerId, status);
        if ("Waiting".equals(status)) {
            queueLength.incrementAndGet();
        } else if ("Cleared".equals(status) || "Denied".equals(status)) {
            queueLength.decrementAndGet();
        }
        logger.info("Passenger {} screening status updated to {}. Queue length: {}", passengerId, status, queueLength.get());
    }
}
