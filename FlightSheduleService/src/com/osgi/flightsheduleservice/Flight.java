package com.osgi.flightsheduleservice;

import java.util.Date;

public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private Date scheduledDeparture;
    private Date estimatedDeparture;
    private Date scheduledArrival;
    private Date estimatedArrival;
    private String status;
    private String terminal;
    private String gate;
    private String aircraftType;

    public Flight(String flightNumber, String airline, String origin, String destination,
                  Date scheduledDeparture, Date estimatedDeparture,
                  Date scheduledArrival, Date estimatedArrival,
                  String status, String terminal, String gate, String aircraftType) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.scheduledDeparture = scheduledDeparture;
        this.estimatedDeparture = estimatedDeparture;
        this.scheduledArrival = scheduledArrival;
        this.estimatedArrival = estimatedArrival;
        this.status = status;
        this.terminal = terminal;
        this.gate = gate;
        this.aircraftType = aircraftType;
    }

    // Getters and setters
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public Date getScheduledDeparture() { return scheduledDeparture; }
    public Date getEstimatedDeparture() { return estimatedDeparture; }
    public Date getScheduledArrival() { return scheduledArrival; }
    public Date getEstimatedArrival() { return estimatedArrival; }
    public String getStatus() { return status; }
    public String getTerminal() { return terminal; }
    public String getGate() { return gate; }
    public String getAircraftType() { return aircraftType; }

    public void setStatus(String status) { this.status = status; }
    public void setGate(String gate) { this.gate = gate; }
    public void setEstimatedDeparture(Date estimatedDeparture) { this.estimatedDeparture = estimatedDeparture; }
    public void setEstimatedArrival(Date estimatedArrival) { this.estimatedArrival = estimatedArrival; }
}
