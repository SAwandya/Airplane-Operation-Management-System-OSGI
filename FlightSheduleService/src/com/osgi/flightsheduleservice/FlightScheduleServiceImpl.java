package com.osgi.flightsheduleservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FlightScheduleServiceImpl implements FlightScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(FlightScheduleServiceImpl.class);
    private final Map<String, Flight> flights = new ConcurrentHashMap<>();
    private final List<FlightScheduleListener> listeners = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    public FlightScheduleServiceImpl() {
        initializeFlights();
        startFlightUpdateSimulator();
    }

    private void initializeFlights() {
        long now = System.currentTimeMillis();
        flights.put("AA123", new Flight("AA123", "American Airlines", "LAX", "JFK",
                new Date(now + 3_600_000), new Date(now + 3_600_000),
                new Date(now + 18_000_000), new Date(now + 18_000_000),
                "Scheduled", "T1", "G12", "Boeing 737"));
        flights.put("UA456", new Flight("UA456", "United Airlines", "SFO", "ORD",
                new Date(now + 7_200_000), new Date(now + 7_200_000),
                new Date(now + 14_400_000), new Date(now + 14_400_000),
                "Scheduled", "T3", "G22", "Airbus A320"));
        flights.put("DL789", new Flight("DL789", "Delta Airlines", "ATL", "MIA",
                new Date(now + 5_400_000), new Date(now + 5_400_000),
                new Date(now + 9_000_000), new Date(now + 9_000_000),
                "Delayed", "T2", "G15", "Boeing 757"));
        logger.info("Initialized {} flights", flights.size());
    }

    private void startFlightUpdateSimulator() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                List<String> flightKeys = new ArrayList<>(flights.keySet());
                if (!flightKeys.isEmpty()) {
                    String flightNumber = flightKeys.get(random.nextInt(flightKeys.size()));
                    Flight flight = flights.get(flightNumber);
                    updateFlightStatusOrGate(flight);
                }
            } catch (Exception e) {
                logger.error("Error in flight update simulator", e);
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    private void updateFlightStatusOrGate(Flight flight) {
        String currentStatus = flight.getStatus();
        String[] statuses = {"Scheduled", "Delayed", "Boarding", "Departed", "Arrived", "Cancelled"};
        String[] gates = {"G10", "G11", "G12", "G13", "G14", "G15", "G20", "G21", "G22"};

        if (random.nextBoolean()) {
            String newStatus = statuses[random.nextInt(statuses.length)];
            if (!newStatus.equals(currentStatus) && isValidStatusTransition(currentStatus, newStatus)) {
                flight.setStatus(newStatus);
                logger.info("Flight {} status updated to {}", flight.getFlightNumber(), newStatus);
                notifyStatusChange(flight.getFlightNumber(), newStatus);
            }
        } else {
            String newGate = gates[random.nextInt(gates.length)];
            if (!newGate.equals(flight.getGate())) {
                flight.setGate(newGate);
                logger.info("Flight {} gate updated to {}", flight.getFlightNumber(), newGate);
                notifyGateChange(flight.getFlightNumber(), newGate);
            }
        }
        notifyFlightUpdate(flight);
    }

    private boolean isValidStatusTransition(String from, String to) {
        Map<String, List<String>> transitions = new HashMap<>();
        transitions.put("Scheduled", Arrays.asList("Delayed", "Boarding"));
        transitions.put("Delayed", Arrays.asList("Scheduled", "Boarding"));
        transitions.put("Boarding", Arrays.asList("Departed"));
        transitions.put("Departed", Arrays.asList("Arrived"));
        transitions.put("Arrived", Collections.emptyList());
        transitions.put("Cancelled", Collections.emptyList());
        return transitions.getOrDefault(from, Collections.emptyList()).contains(to);
    }

    @Override
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }

    @Override
    public Flight getFlightByNumber(String flightNumber) {
        return flights.get(flightNumber);
    }

    @Override
    public boolean updateFlight(Flight flight) {
        if (flight == null || flight.getFlightNumber() == null) {
            logger.warn("Invalid flight update attempt: {}", flight);
            return false;
        }
        Flight oldFlight = flights.put(flight.getFlightNumber(), flight);
        if (oldFlight != null) {
            if (!oldFlight.getStatus().equals(flight.getStatus())) {
                notifyStatusChange(flight.getFlightNumber(), flight.getStatus());
            }
            if (!oldFlight.getGate().equals(flight.getGate())) {
                notifyGateChange(flight.getFlightNumber(), flight.getGate());
            }
        }
        notifyFlightUpdate(flight);
        logger.info("Flight {} updated successfully", flight.getFlightNumber());
        return true;
    }

    @Override
    public void registerListener(FlightScheduleListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            logger.debug("Listener registered: {}", listener.getClass().getName());
        }
    }

    @Override
    public void unregisterListener(FlightScheduleListener listener) {
        if (listener != null) {
            listeners.remove(listener);
            logger.debug("Listener unregistered: {}", listener.getClass().getName());
        }
    }

    private void notifyStatusChange(String flightNumber, String newStatus) {
        for (FlightScheduleListener listener : listeners) {
            listener.onStatusChanged(flightNumber, newStatus);
        }
    }

    private void notifyGateChange(String flightNumber, String newGate) {
        for (FlightScheduleListener listener : listeners) {
            listener.onGateChanged(flightNumber, newGate);
        }
    }

    private void notifyFlightUpdate(Flight flight) {
        for (FlightScheduleListener listener : listeners) {
            listener.onFlightUpdated(flight);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("FlightScheduleServiceImpl shut down");
    }
}
