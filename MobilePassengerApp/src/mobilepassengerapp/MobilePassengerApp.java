package mobilepassengerapp;

import org.osgi.framework.BundleContext;

import java.util.Scanner;

import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baggagehandlingservice.BaggageHandlingService;
import baggagehandlingservice.BaggageHandlingServiceImpl;

public class MobilePassengerApp {
    private BaggageHandlingService baggageService;
    private Scanner scanner;

    // Constructor for non-OSGi usage
    public MobilePassengerApp() {
        this.baggageService = new BaggageHandlingServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    // Constructor for OSGi usage
    public MobilePassengerApp(BundleContext context) {
        this.scanner = new Scanner(System.in);
        ServiceReference<BaggageHandlingService> ref = context.getServiceReference(BaggageHandlingService.class);
        if (ref != null) {
            this.baggageService = context.getService(ref);
        }
    }

    public void start() {
        if (baggageService == null) {
            System.out.println("Baggage Handling Service is currently unavailable.");
            return;
        }
        System.out.println("========================================");
        System.out.println("Welcome to the Mobile Passenger App");
        System.out.println("Track your baggage status on the go!");
        System.out.println("========================================");

        boolean continueLoop = true;
        while (continueLoop) {
            String passengerId = askForPassengerId();
            String flightNumber = askForFlightNumber();
            getBaggageStatus(passengerId, flightNumber);
            continueLoop = askToContinue();
        }
        System.out.println("Thank you for using the Mobile Passenger App. Safe travels!");
    }

    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("MobilePassengerApp stopped");
    }

    private String askForPassengerId() {
        System.out.print("Enter your passenger ID (e.g., P001): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || !input.matches("P\\d{3}")) {
            System.out.println("Invalid passenger ID. It should be 'P' followed by 3 digits (e.g., P001). Try again.");
            return askForPassengerId();
        }
        return input;
    }

    private String askForFlightNumber() {
        System.out.print("Enter your flight number (e.g., FL123): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() || !input.matches("FL\\d{3}")) {
            System.out.println("Invalid flight number. It should be 'FL' followed by 3 digits (e.g., FL123). Try again.");
            return askForFlightNumber();
        }
        return input;
    }

    private void getBaggageStatus(String passengerId, String flightNumber) {
        String status = baggageService.getBaggageStatus(passengerId, flightNumber);
        System.out.println("\n--- Baggage Status ---");
        System.out.println("Passenger ID: " + passengerId);
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

    // Additional method: Display help
    public void showHelp() {
        System.out.println("\n--- Help ---");
        System.out.println("This app helps you track your baggage status.");
        System.out.println("- Passenger ID format: 'P' followed by 3 digits (e.g., P001).");
        System.out.println("- Flight number format: 'FL' followed by 3 digits (e.g., FL123).");
        System.out.println("Follow the prompts to check your baggage status.");
    }

    // Additional method: Reset scanner (useful for testing or reinitialization)
    public void resetScanner() {
        if (scanner != null) {
            scanner.close();
        }
        scanner = new Scanner(System.in);
        System.out.println("Scanner reset.");
    }
}
