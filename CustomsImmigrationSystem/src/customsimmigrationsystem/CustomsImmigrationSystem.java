package customsimmigrationsystem;

import java.util.Scanner;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import passengercheckinservice.PassengerCheckInService;
import securitycheckservice.SecurityCheckService;


public class CustomsImmigrationSystem {
    private final SecurityCheckService securityService;
    private final PassengerCheckInService checkInService;
    private final Scanner scanner = new Scanner(System.in);

    // Constructor for standalone use (can be adapted for OSGi)
    public CustomsImmigrationSystem(SecurityCheckService securityService, PassengerCheckInService checkInService) {
        this.securityService = securityService;
        this.checkInService = checkInService;
    }

    // Start the system
    public void start() {
        System.out.println("Welcome to the Customs & Immigration System");
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    checkPassengerClearance();
                    break;
                case "2":
                    viewSecurityQueueLength();
                    break;
                case "3":
                    displayServiceStatus();
                    break;
                case "4":
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
        System.out.println("1. Check passenger clearance");
        System.out.println("2. View security queue length");
        System.out.println("3. Check service status");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    // Stop the system and clean up
    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
    }

    // Check passenger clearance with interactive flow
    private void checkPassengerClearance() {
        System.out.print("Enter passenger ID: ");
        String passengerId = scanner.nextLine().trim();
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();

        if (securityService != null && checkInService != null) {
            String screeningStatus = securityService.getScreeningStatus(passengerId);
            boolean isCheckedIn = checkInService.isCheckedIn(passengerId, flightNumber);
            String seat = checkInService.getSeat(passengerId, flightNumber);

            System.out.println("\nPassenger Clearance Information:");
            System.out.println("Passenger ID: " + passengerId);
            System.out.println("Flight Number: " + flightNumber);
            System.out.println("Screening Status: " + screeningStatus);
            System.out.println("Checked In: " + (isCheckedIn ? "Yes" : "No"));
            System.out.println("Seat: " + seat);

            // Interactive question to update screening status
            System.out.print("Would you like to update the screening status? (yes/no): ");
            String updateChoice = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(updateChoice)) {
                System.out.print("Enter new status (Waiting/Cleared/Denied): ");
                String newStatus = scanner.nextLine().trim();
                securityService.updateScreeningStatus(passengerId, newStatus);
                System.out.println("Screening status updated to: " + newStatus);
            }
        } else {
            System.out.println("Required services are unavailable.");
        }
    }

    // View security queue length
    private void viewSecurityQueueLength() {
        if (securityService != null) {
            int queueLength = securityService.getQueueLength();
            System.out.println("\nCurrent Security Queue Length: " + queueLength + " passengers");
        } else {
            System.out.println("Security Check Service unavailable.");
        }
    }

    // Display service status
    private void displayServiceStatus() {
        System.out.println("\nService Availability:");
        System.out.println("Security Check Service: " + (securityService != null ? "Available" : "Unavailable"));
        System.out.println("Passenger Check-In Service: " + (checkInService != null ? "Available" : "Unavailable"));
    }

    // Main method for standalone testing
    public static void main(String[] args) {
        SecurityCheckService securityService = new securitycheckservice.SecurityCheckServiceImpl();
        PassengerCheckInService checkInService = new passengercheckinservice.PassengerCheckInServiceImpl();
        CustomsImmigrationSystem system = new CustomsImmigrationSystem(securityService, checkInService);
        system.start();
    }
}
