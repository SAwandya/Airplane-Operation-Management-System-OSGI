package airlineoperationsdashboard;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.Flight;
import com.osgi.flightsheduleservice.FlightScheduleService;

import groundsupportservice.GroundSupportService;
import weatherairtrafficservice.WeatherAirTrafficService;


public class AirlineOperationsDashboard {
    private FlightScheduleService flightService;
    private GroundSupportService groundService;
    private WeatherAirTrafficService weatherService;
    private Scanner scanner = new Scanner(System.in);
    private List<String> notifications = new ArrayList<>();

    // Constructor to retrieve services from OSGi context
    public AirlineOperationsDashboard(BundleContext context) {
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        }
        ServiceReference<GroundSupportService> groundRef = context.getServiceReference(GroundSupportService.class);
        if (groundRef != null) {
            groundService = context.getService(groundRef);
        }
        ServiceReference<WeatherAirTrafficService> weatherRef = context.getServiceReference(WeatherAirTrafficService.class);
        if (weatherRef != null) {
            weatherService = context.getService(weatherRef);
        }
    }

    // Start the dashboard
    public void start() {
        if (flightService != null) {
//            flightService.registerListener(this);
        }
        System.out.println("Welcome to Airline Operations Dashboard");
        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. View all flights");
            System.out.println("2. Check specific flight");
            System.out.println("3. Update ground support status");
            System.out.println("4. Check notifications");
            System.out.println("5. Check service status");
            System.out.println("6. View current weather");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewAllFlights();
                    break;
                case "2":
                    checkSpecificFlight();
                    break;
                case "3":
                    updateGroundSupportStatus();
                    break;
                case "4":
                    checkNotifications();
                    break;
                case "5":
                    displayServiceStatus();
                    break;
                case "6":
                    viewCurrentWeather();
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Goodbye!");
        stop();
    }

    // Stop the dashboard and clean up
    public void stop() {
        if (flightService != null) {
//            flightService.unregisterListener(this);
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    // View all flights with selection option
    private void viewAllFlights() {
        if (flightService != null) {
            List<Flight> flights = flightService.getAllFlights();
            System.out.println("\nAll Flights:");
            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                String groundStatus = (groundService != null) ? groundService.getGroundSupportStatus(flight.getFlightNumber()) : "N/A";
                System.out.println((i + 1) + ". " + flight.getFlightNumber() + " - " + flight.getStatus() +
                        " - Gate: " + flight.getGate() + " - Ground: " + groundStatus);
            }
            System.out.print("Enter the number to check details (or 0 to go back): ");
            String input = scanner.nextLine().trim();
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < flights.size()) {
                    Flight selected = flights.get(index);
                    checkSpecificFlight(selected.getFlightNumber());
                } else if (!input.equals("0")) {
                    System.out.println("Invalid selection");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        } else {
            System.out.println("Flight Schedule Service unavailable");
        }
    }

    // Check specific flight by asking for flight number
    private void checkSpecificFlight() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        checkSpecificFlight(flightNumber);
    }

    // Display detailed flight information
    private void checkSpecificFlight(String flightNumber) {
        if (flightService != null) {
            Flight flight = flightService.getFlightByNumber(flightNumber);
            if (flight != null) {
                System.out.println("\nFlight Details:");
                System.out.println("Flight Number: " + flight.getFlightNumber());
                System.out.println("Airline: " + flight.getAirline());
                System.out.println("Origin: " + flight.getOrigin());
                System.out.println("Destination: " + flight.getDestination());
                System.out.println("Scheduled Departure: " + flight.getScheduledDeparture());
                System.out.println("Estimated Departure: " + flight.getEstimatedDeparture());
                System.out.println("Scheduled Arrival: " + flight.getScheduledArrival());
                System.out.println("Estimated Arrival: " + flight.getEstimatedArrival());
                System.out.println("Status: " + flight.getStatus());
                System.out.println("Terminal: " + flight.getTerminal());
                System.out.println("Gate: " + flight.getGate());
                System.out.println("Aircraft Type: " + flight.getAircraftType());
                if (groundService != null) {
                    System.out.println("Ground Support Status: " + groundService.getGroundSupportStatus(flightNumber));
                } else {
                    System.out.println("Ground Support Service unavailable");
                }
                if (weatherService != null) {
                    System.out.println("Weather Report: " + weatherService.getWeatherReport());
                    System.out.println("Flight Path Adjustment: " + weatherService.getFlightPathAdjustment(flightNumber));
                } else {
                    System.out.println("Weather & Air Traffic Service unavailable");
                }
            } else {
                System.out.println("Flight not found");
            }
        } else {
            System.out.println("Flight Schedule Service unavailable");
        }
    }

    // Update ground support status
    private void updateGroundSupportStatus() {
        if (groundService == null) {
            System.out.println("Ground Support Service unavailable");
            return;
        }
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        System.out.print("Enter new ground support status: ");
        String status = scanner.nextLine().trim();
        groundService.updateGroundSupportStatus(flightNumber, status);
        System.out.println("Updated ground support status for flight " + flightNumber + " to " + status);
    }

    // Check and display notifications
    private void checkNotifications() {
        if (notifications.isEmpty()) {
            System.out.println("No new notifications");
        } else {
            System.out.println("Notifications:");
            for (String notification : notifications) {
                System.out.println("- " + notification);
            }
            notifications.clear();
        }
    }

    // Display service availability
    private void displayServiceStatus() {
        System.out.println("\nService Availability:");
        System.out.println("Flight Schedule Service: " + (flightService != null ? "Available" : "Unavailable"));
        System.out.println("Ground Support Service: " + (groundService != null ? "Available" : "Unavailable"));
        System.out.println("Weather & Air Traffic Service: " + (weatherService != null ? "Available" : "Unavailable"));
    }

    // View current weather report
    private void viewCurrentWeather() {
        if (weatherService != null) {
            System.out.println("\nCurrent Weather: " + weatherService.getWeatherReport());
        } else {
            System.out.println("Weather & Air Traffic Service unavailable");
        }
    }


}
