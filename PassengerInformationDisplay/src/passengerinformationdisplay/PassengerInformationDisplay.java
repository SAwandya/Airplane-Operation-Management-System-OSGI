
package passengerinformationdisplay;

import com.osgi.flightsheduleservice.Flight;
import com.osgi.flightsheduleservice.FlightScheduleService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import securitycheckservice.SecurityCheckService;

import java.util.Scanner;

public class PassengerInformationDisplay {
//    private static final Logger logger = LoggerFactory.getLogger(PassengerInformationDisplay.class);
    private final BundleContext context;
    private FlightScheduleService flightService;
    private SecurityCheckService securityService;
    private Scanner scanner;

    public PassengerInformationDisplay(BundleContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Look up FlightScheduleService
        ServiceReference<FlightScheduleService> flightRef = context.getServiceReference(FlightScheduleService.class);
        if (flightRef != null) {
            flightService = context.getService(flightRef);
        } else {
//            logger.warn("FlightScheduleService not available");
        }

        // Look up SecurityCheckService
        ServiceReference<SecurityCheckService> securityRef = context.getServiceReference(SecurityCheckService.class);
        if (securityRef != null) {
            securityService = context.getService(securityRef);
        } else {
//            logger.warn("SecurityCheckService not available");
        }

        // Check if both services are unavailable
        if (flightService == null && securityService == null) {
            System.out.println("No services available. Exiting.");
            return;
        }

        // Start the interactive session
        startInteractiveMode();
    }

    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
//        logger.info("PassengerInformationDisplay stopped");
    }

    /** Initiates the interactive terminal session */
    private void startInteractiveMode() {
        System.out.println("========================================");
        System.out.println("Welcome to the Passenger Information Display");
        System.out.println("This system provides flight and security check information.");
        System.out.println("========================================");

        boolean continueLoop = true;
        while (continueLoop) {
            String choice = askForChoice();
            processUserChoice(choice);
            continueLoop = askToContinue();
        }
        System.out.println("Thank you for using the Passenger Information Display. Goodbye!");
    }

    /** Asks the user what information they want */
    private String askForChoice() {
        System.out.println("\nWhat would you like to know?");
        System.out.println("1. Flight status");
        System.out.println("2. Security check status");
        System.out.println("3. Both flight and security status");
        System.out.print("Enter your choice (1-3): ");
        return scanner.nextLine().trim();
    }

    /** Processes the user's choice and directs the flow */
    private void processUserChoice(String choice) {
        switch (choice) {
            case "1":
                if (flightService != null) {
                    String flightNumber = askForFlightNumber();
                    getFlightInfo(flightNumber);
                } else {
                    System.out.println("Flight Schedule Service is currently unavailable.");
                }
                break;
            case "2":
                if (securityService != null) {
                    String passengerId = askForPassengerId();
                    getSecurityInfo(passengerId);
                } else {
                    System.out.println("Security Check Service is currently unavailable.");
                }
                break;
            case "3":
                if (flightService != null && securityService != null) {
                    String flightNumber = askForFlightNumber();
                    getFlightInfo(flightNumber);
                    String passengerId = askForPassengerId();
                    getSecurityInfo(passengerId);
                } else {
                    System.out.println("One or both services are unavailable.");
                }
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    /** Prompts for and validates the flight number */
    private String askForFlightNumber() {
        System.out.print("Please enter the flight number (e.g., FL123): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Flight number cannot be empty. Please try again.");
            return askForFlightNumber(); // Recursive call for valid input
        }
        return input;
    }

    /** Prompts for and validates the passenger ID */
    private String askForPassengerId() {
        System.out.print("Please enter the passenger ID (e.g., P123): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Passenger ID cannot be empty. Please try again.");
            return askForPassengerId(); // Recursive call for valid input
        }
        return input;
    }

    /** Retrieves and displays flight information */
    private void getFlightInfo(String flightNumber) {
        Flight flight = flightService.getFlightByNumber(flightNumber);
        System.out.println("\n--- Flight Information ---");
        if (flight != null) {
            System.out.println("Flight Number: " + flight.getFlightNumber());
            System.out.println("Status: " + flight.getStatus());
            System.out.println("Gate: " + flight.getGate());
            System.out.println("Estimated Departure: " + flight.getEstimatedDeparture());
            System.out.println("Estimated Arrival: " + flight.getEstimatedArrival());
        } else {
            System.out.println("No information found for flight " + flightNumber + ".");
        }
    }

    /** Retrieves and displays security information */
    private void getSecurityInfo(String passengerId) {
        String screeningStatus = securityService.getScreeningStatus(passengerId);
        int queueLength = securityService.getQueueLength();
        System.out.println("\n--- Security Information ---");
        System.out.println("Passenger ID: " + passengerId);
        System.out.println("Screening Status: " + screeningStatus);
        System.out.println("Current Security Queue Length: " + queueLength + " passengers");
    }

    /** Asks if the user wants to continue */
    private boolean askToContinue() {
        System.out.print("\nWould you like to check another flight or passenger? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        while (!response.equals("yes") && !response.equals("no") && !response.equals("y") && !response.equals("n")) {
            System.out.print("Please enter 'yes' or 'no': ");
            response = scanner.nextLine().trim().toLowerCase();
        }
        return response.equals("yes") || response.equals("y");
    }

    /** Displays service availability status (additional method) */
    public void displayServiceStatus() {
        System.out.println("Service Availability:");
        System.out.println("Flight Schedule Service: " + (flightService != null ? "Available" : "Unavailable"));
        System.out.println("Security Check Service: " + (securityService != null ? "Available" : "Unavailable"));
    }
    
}