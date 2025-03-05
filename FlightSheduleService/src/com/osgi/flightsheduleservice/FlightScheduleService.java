package com.osgi.flightsheduleservice;

import java.util.List;

public interface FlightScheduleService {
    List<Flight> getAllFlights();
    Flight getFlightByNumber(String flightNumber);
    boolean updateFlight(Flight flight);
    void registerListener(FlightScheduleListener listener);
    void unregisterListener(FlightScheduleListener listener);
}
