package baggagehandlingservice;


public class Baggage {
    private String baggageId;
    private String status;
    private String location;
    private String flightNumber;
    private String history; // Simplified history as a string

    public Baggage(String baggageId, String status, String location, String flightNumber, String history) {
        this.baggageId = baggageId;
        this.status = status;
        this.location = location;
        this.flightNumber = flightNumber;
        this.history = history;
    }

    // Getters
    public String getBaggageId() { return baggageId; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getFlightNumber() { return flightNumber; }
    public String getHistory() { return history; }

    // Setter for status updates
    public void setStatus(String status) { this.status = status; }
    public void appendHistory(String update) { this.history += " | " + update; }
}
