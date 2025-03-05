package com.osgi.flightsheduleservice;

public interface FlightScheduleListener {
    void onStatusChanged(String flightNumber, String newStatus);
    void onGateChanged(String flightNumber, String newGate);
    void onFlightUpdated(Flight flight);
}
