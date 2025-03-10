package baggagetrackingsystem;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baggagehandlingservice.BaggageHandlingService;

import java.util.Scanner;

public class BaggageTrackingSystem {
//    private static final Logger logger = LoggerFactory.getLogger(BaggageTrackingSystem.class);
    private final BundleContext context;
    private BaggageHandlingService baggageService;
    private Scanner scanner;

    public BaggageTrackingSystem(BundleContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        ServiceReference<BaggageHandlingService> ref = context.getServiceReference(BaggageHandlingService.class);
        if (ref != null) {
            baggageService = context.getService(ref);
            startInteractiveMode();
        } else {
//            logger.warn("BaggageHandlingService not available");
            System.out.println("Baggage Handling Service is currently unavailable.");
        }
    }

    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
//        logger.info("BaggageTrackingSystem stopped");
    }

    private void startInteractiveMode() {
        System.out.println("========================================");
        System.out.println("Welcome to the Baggage Tracking System");
        System.out.println("Track your baggage status easily!");
        System.out.println("========================================");

        boolean continueLoop = true;
        while (continueLoop) {
            String choice = askForSearchMethod();
            if (choice.equals("0")) {
                showHelp();
            } else {
                processUserChoice(choice);
            }
            continueLoop = askToContinue();
        }
        System.out.println("Thank you for using the Baggage Tracking System. Goodbye!");
    }

    private String askForSearchMethod() {
        System.out.println("\nHow would you like to search for your baggage?");
        System.out.println("1. By baggage tag number");
        System.out.println("2. By passenger name and flight number");
        System.out.println("0. Show help");
        System.out.print("Enter your choice (0, 1, or 2): ");
        String choice = scanner.nextLine().trim();
        if (!choice.equals("0") && !choice.equals("1") && !choice.equals("2")) {
            System.out.println("Invalid choice. Please try again.");
            return askForSearchMethod();
        }
        return choice;
    }

    private void processUserChoice(String choice) {
        switch (choice) {
            case "1":
                String tagNumber = askForTagNumber();
                getBaggageStatusByTag(tagNumber);
                break;
            case "2":
                String passengerName = askForPassengerName();
                String flightNumber = askForFlightNumber();
                getBaggageStatusByPassenger(passengerName, flightNumber);
                break;
        }
    }

    private String askForTagNumber() {
        System.out.print("Enter baggage tag number (e.g., TAG123): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || !input.matches("TAG\\d{3}")) {
            System.out.println("Invalid tag number. It should be like 'TAG123'. Try again.");
            return askForTagNumber();
        }
        return input;
    }

    private String askForPassengerName() {
        System.out.print("Enter passenger's full name (e.g., John Doe): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || !input.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid name. Use letters and spaces only. Try again.");
            return askForPassengerName();
        }
        return input;
    }

    private String askForFlightNumber() {
        System.out.print("Enter flight number (e.g., FL123): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || !input.matches("FL\\d{3}")) {
            System.out.println("Invalid flight number. It should be like 'FL123'. Try again.");
            return askForFlightNumber();
        }
        return input;
    }

    private void getBaggageStatusByTag(String tagNumber) {
        String status = baggageService.getBaggageStatusByTag(tagNumber);
        System.out.println("\n--- Baggage Status ---");
        System.out.println("Tag Number: " + tagNumber);
        System.out.println("Status: " + status);
    }

    private void getBaggageStatusByPassenger(String passengerName, String flightNumber) {
        String status = baggageService.getBaggageStatusByPassenger(passengerName, flightNumber);
        System.out.println("\n--- Baggage Status ---");
        System.out.println("Passenger: " + passengerName);
        System.out.println("Flight Number: " + flightNumber);
        System.out.println("Status: " + status);
    }

    private boolean askToContinue() {
        System.out.print("\nCheck another baggage? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        while (!response.equals("yes") && !response.equals("no") && !response.equals("y") && !response.equals("n")) {
            System.out.print("Please enter 'yes' or 'no': ");
            response = scanner.nextLine().trim().toLowerCase();
        }
        return response.equals("yes") || response.equals("y");
    }

    // Additional method: Show help
    private void showHelp() {
        System.out.println("\n--- Help ---");
        System.out.println("This system helps you track baggage status.");
        System.out.println("- Option 1: Search by baggage tag (format: TAG followed by 3 digits, e.g., TAG123)");
        System.out.println("- Option 2: Search by passenger name and flight number (e.g., John Doe, FL123)");
        System.out.println("Follow the prompts to enter details.");
    }
}