package emergencyresponsesystem;

import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.Flight;
import com.osgi.flightsheduleservice.FlightScheduleService;

import weatherairtrafficservice.WeatherAirTrafficService;


public class EmergencyResponseSystem {
    private FlightScheduleService flightService;
    private WeatherAirTrafficService weatherService;
    private Scanner scanner = new Scanner(System.in);

    // Constructor to retrieve services from OSGi context
    public EmergencyResponseSystem(BundleContext context) {
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        }
        ServiceReference<WeatherAirTrafficService> weatherRef = context.getServiceReference(WeatherAirTrafficService.class);
        if (weatherRef != null) {
            weatherService = context.getService(weatherRef);
        }
    }

    // Start the system
    public void start() {
        System.out.println("Welcome to Emergency Response System");
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    assessFlightSituation();
                    break;
                case "2":
                    checkSpecificFlight();
                    break;
                case "3":
                    viewCurrentWeather();
                    break;
                case "4":
                    viewAllFlights();
                    break;
                case "5":
                    displayServiceStatus();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Goodbye!");
        stop();
    }

    // Display the main menu
    private void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Assess flight situation");
        System.out.println("2. Check specific flight");
        System.out.println("3. View current weather");
        System.out.println("4. View all flights");
        System.out.println("5. Check service status");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    // Stop the system and clean up
    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
    }

    // Assess flight situation
    private void assessFlightSituation() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        if (flightService != null && weatherService != null) {
            Flight flight = flightService.getFlightByNumber(flightNumber);
            if (flight != null) {
                System.out.println("\nFlight Situation Assessment:");
                System.out.println("Flight Number: " + flight.getFlightNumber());
                System.out.println("Status: " + flight.getStatus());
                System.out.println("Weather Report: " + weatherService.getWeatherReport());
                System.out.println("Flight Path Adjustment: " + weatherService.getFlightPathAdjustment(flightNumber));
            } else {
                System.out.println("Flight not found");
            }
        } else {
            System.out.println("Required services unavailable");
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

    // View current weather report
    private void viewCurrentWeather() {
        if (weatherService != null) {
            System.out.println("\nCurrent Weather: " + weatherService.getWeatherReport());
        } else {
            System.out.println("Weather & Air Traffic Service unavailable");
        }
    }

    // View all flights with selection option
    private void viewAllFlights() {
        if (flightService != null) {
            List<Flight> flights = flightService.getAllFlights();
            System.out.println("\nAll Flights:");
            for (int i = 0; i < flights.size(); i++) {
                Flight flight = flights.get(i);
                System.out.println((i + 1) + ". " + flight.getFlightNumber() + " - " + flight.getStatus() +
                        " - Gate: " + flight.getGate());
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

    // Display service availability
    private void displayServiceStatus() {
        System.out.println("\nService Availability:");
        System.out.println("Flight Schedule Service: " + (flightService != null ? "Available" : "Unavailable"));
        System.out.println("Weather & Air Traffic Service: " + (weatherService != null ? "Available" : "Unavailable"));
    }
}