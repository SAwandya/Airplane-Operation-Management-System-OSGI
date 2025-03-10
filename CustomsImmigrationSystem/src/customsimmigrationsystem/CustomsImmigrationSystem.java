package customsimmigrationsystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.flightsheduleservice.Flight;
import com.osgi.flightsheduleservice.FlightScheduleService;

import baggagehandlingservice.BaggageHandlingService;
import passengercheckinservice.PassengerCheckInService;
import passengerprofilingservice.PassengerProfilingService;
import securitycheckservice.SecurityCheckService;
import visaverificationservice.VisaVerificationService;


public class CustomsImmigrationSystem {
    private final SecurityCheckService securityService;
    private final PassengerCheckInService checkInService;
    private final FlightScheduleService flightService;
    private final PassengerProfilingService profilingService;
    private final VisaVerificationService visaService;
    private final BaggageHandlingService baggageService;
    private Scanner scanner;
    private final List<String> notifications = new ArrayList<>();
    private final List<String> processedPassengers = new ArrayList<>(); // For reporting

    public CustomsImmigrationSystem(SecurityCheckService securityService, PassengerCheckInService checkInService,
                                    FlightScheduleService flightService, PassengerProfilingService profilingService,
                                    VisaVerificationService visaService, BaggageHandlingService baggageService) {
        this.securityService = securityService;
        this.checkInService = checkInService;
        this.flightService = flightService;
        this.profilingService = profilingService;
        this.visaService = visaService;
        this.baggageService = baggageService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=======================================");
        System.out.println("Welcome to the Advanced Customs & Immigration System");
        System.out.println("=======================================");
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": checkPassengerClearance(); break;
                case "2": checkInPassenger(); break;
                case "3": batchProcessFlight(); break;
                case "4": viewSecurityQueueLength(); break;
                case "5": viewNotifications(); break;
                case "6": generateProcessingReport(); break;
                case "7": displayServiceStatus(); break;
                case "8": displayHelp(); break;
                case "9": running = false; break;
                default: System.out.println("Invalid choice. Please enter 1-9.");
            }
        }
        System.out.println("=======================================");
        System.out.println("Goodbye!");
        stop();
    }

    private void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Check passenger clearance");
        System.out.println("2. Check in passenger");
        System.out.println("3. Batch process passengers by flight");
        System.out.println("4. View security queue length");
        System.out.println("5. View notifications");
        System.out.println("6. Generate processing report");
        System.out.println("7. Check service status");
        System.out.println("8. View help");
        System.out.println("9. Exit");
        System.out.print("Enter your choice (1-9): ");
    }

    public void stop() {
        if (scanner != null) {
            scanner.close();
        }
    }

    private void checkPassengerClearance() {
        String passengerId = promptForPassengerId();
        String flightNumber = promptForFlightNumber();

        if (!validateFlightNumber(flightNumber)) {
            System.out.println("Flight not found in schedule. Please check the flight number.");
            askToContinue();
            return;
        }

        displayPassengerInfo(passengerId, flightNumber);
        handlePassengerActions(passengerId, flightNumber);
        processedPassengers.add(passengerId + " (" + flightNumber + ")");
        askToContinue();
    }

    private String promptForPassengerId() {
        System.out.print("Enter passenger ID (e.g., P123): ");
        String passengerId = scanner.nextLine().trim();
        if (!passengerId.matches("P\\d{3}")) {
            System.out.println("Invalid format. Use 'P' followed by 3 digits (e.g., P123).");
            return promptForPassengerId();
        }
        return passengerId;
    }

    private String promptForFlightNumber() {
        System.out.print("Enter flight number (e.g., FL123): ");
        String flightNumber = scanner.nextLine().trim();
        if (!flightNumber.matches("FL\\d{3}")) {
            System.out.println("Invalid format. Use 'FL' followed by 3 digits (e.g., FL123).");
            return promptForFlightNumber();
        }
        return flightNumber;
    }

    private boolean validateFlightNumber(String flightNumber) {
        return flightService != null && flightService.getFlightByNumber(flightNumber) != null;
    }

    private void displayPassengerInfo(String passengerId, String flightNumber) {
        if (!checkServicesAvailable()) {
            System.out.println("Required services are unavailable.");
            return;
        }

        String screeningStatus = securityService.getScreeningStatus(passengerId);
        boolean isCheckedIn = checkInService.isCheckedIn(passengerId, flightNumber);
        String seat = checkInService.getSeat(passengerId, flightNumber);
        Flight flight = flightService.getFlightByNumber(flightNumber);
        int riskScore = profilingService.getRiskScore(passengerId);
        String riskPrediction = profilingService.predictRiskLevel(passengerId, flightNumber, screeningStatus);
        String visaStatus = visaService.checkVisaStatus(passengerId, flight.getDestination());
        String baggageStatus = baggageService.getBaggageStatus(passengerId, flightNumber);
        int queueLength = securityService.getQueueLength();

        System.out.println("\n=== Passenger Clearance Information ===");
        System.out.println("Passenger ID: " + passengerId);
        System.out.println("Flight Number: " + flightNumber);
        System.out.println("Destination: " + flight.getDestination());
        System.out.println("Flight Status: " + flight.getStatus());
        System.out.println("Screening Status: " + screeningStatus);
        System.out.println("Checked In: " + (isCheckedIn ? "Yes" : "No"));
        System.out.println("Seat: " + seat);
        System.out.println("Risk Score: " + riskScore + " (0-100)");
        System.out.println("AI Risk Prediction: " + riskPrediction);
        System.out.println("Visa Status: " + visaStatus);
        System.out.println("Baggage Status: " + baggageStatus);
        System.out.println("Security Queue Length: " + queueLength + " passengers");

        // Smart Queue Optimization
        String queueSuggestion = suggestQueueAssignment(riskScore, screeningStatus, visaStatus, baggageStatus, queueLength);
        System.out.println("Queue Suggestion: " + queueSuggestion);

        // Alerts
        if ("No baggage found for this flight".equals(baggageStatus) && isCheckedIn) {
            notifications.add("Passenger " + passengerId + " missing baggage for flight " + flightNumber);
        }
        if (visaStatus.contains("Expired") || visaStatus.contains("No visa")) {
            notifications.add("Passenger " + passengerId + " has visa issues for " + flight.getDestination());
        }
        System.out.println("=======================================");
    }

    private boolean checkServicesAvailable() {
        return securityService != null && checkInService != null && flightService != null &&
               profilingService != null && visaService != null && baggageService != null;
    }

    private String suggestQueueAssignment(int riskScore, String screeningStatus, String visaStatus, String baggageStatus, int queueLength) {
        if (riskScore < 30 && "Cleared".equalsIgnoreCase(screeningStatus) && visaStatus.contains("Valid") &&
            !"No baggage found for this flight".equals(baggageStatus)) {
            return queueLength > 10 ? "Fast-Track Lane" : "Standard Lane";
        } else if (riskScore > 60 || visaStatus.contains("Expired") || visaStatus.contains("No visa") ||
                   "Denied".equalsIgnoreCase(screeningStatus)) {
            return "Secondary Screening";
        } else {
            return "Standard Lane";
        }
    }

    private void handlePassengerActions(String passengerId, String flightNumber) {
        boolean subMenuRunning = true;
        while (subMenuRunning) {
            System.out.println("\n=== Actions ===");
            System.out.println("1. Update screening status");
            System.out.println("2. Check in passenger (if not checked in)");
            System.out.println("3. Auto-approve (if eligible)");
            System.out.println("4. Back to main menu");
            System.out.print("Choose an action (1-4): ");
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1":
                    String oldStatus = securityService.getScreeningStatus(passengerId);
                    updateScreeningStatus(passengerId);
                    String newStatus = securityService.getScreeningStatus(passengerId);
                    if (!oldStatus.equals(newStatus)) {
                        notifications.add("Passenger " + passengerId + " status changed to " + newStatus);
                    }
                    displayPassengerInfo(passengerId, flightNumber);
                    break;
                case "2":
                    if (!checkInService.isCheckedIn(passengerId, flightNumber)) {
                        checkInPassenger(passengerId, flightNumber);
                        displayPassengerInfo(passengerId, flightNumber);
                    } else {
                        System.out.println("Passenger is already checked in.");
                    }
                    break;
                case "3":
                    autoApprovePassenger(passengerId, flightNumber);
                    displayPassengerInfo(passengerId, flightNumber);
                    break;
                case "4":
                    subMenuRunning = false;
                    break;
                default:
                    System.out.println("Invalid action. Please enter 1-4.");
            }
        }
    }

    private void updateScreeningStatus(String passengerId) {
        System.out.print("Enter new status (Waiting/Cleared/Denied): ");
        String newStatus = scanner.nextLine().trim();
        if ("Waiting".equalsIgnoreCase(newStatus) || "Cleared".equalsIgnoreCase(newStatus) || "Denied".equalsIgnoreCase(newStatus)) {
            securityService.updateScreeningStatus(passengerId, newStatus);
            System.out.println("Screening status updated to: " + newStatus);
        } else {
            System.out.println("Invalid status. Use 'Waiting', 'Cleared', or 'Denied'.");
        }
    }

    private void checkInPassenger(String passengerId, String flightNumber) {
        if (checkInService != null) {
            System.out.print("Enter seat number (e.g., 12A): ");
            String seat = scanner.nextLine().trim();
            if (seat.matches("\\d+[A-Z]")) {
                checkInService.checkInPassenger(passengerId, flightNumber, seat);
                System.out.println("Passenger " + passengerId + " checked in for flight " + flightNumber + " with seat " + seat);
            } else {
                System.out.println("Invalid seat format. Use number followed by letter (e.g., 12A).");
            }
        } else {
            System.out.println("Passenger Check-In Service unavailable.");
        }
    }

    private void checkInPassenger() {
        String passengerId = promptForPassengerId();
        String flightNumber = promptForFlightNumber();
        if (validateFlightNumber(flightNumber)) {
            if (!checkInService.isCheckedIn(passengerId, flightNumber)) {
                checkInPassenger(passengerId, flightNumber);
                processedPassengers.add(passengerId + " (" + flightNumber + ")");
            } else {
                System.out.println("Passenger " + passengerId + " is already checked in for flight " + flightNumber + ".");
            }
        } else {
            System.out.println("Flight not found in schedule.");
        }
        askToContinue();
    }

    private void autoApprovePassenger(String passengerId, String flightNumber) {
        if (!checkServicesAvailable()) {
            System.out.println("Cannot auto-approve: Services unavailable.");
            return;
        }
        String screeningStatus = securityService.getScreeningStatus(passengerId);
        int riskScore = profilingService.getRiskScore(passengerId);
        String visaStatus = visaService.checkVisaStatus(passengerId, flightService.getFlightByNumber(flightNumber).getDestination());
        String baggageStatus = baggageService.getBaggageStatus(passengerId, flightNumber);

        if (riskScore < 30 && "Cleared".equalsIgnoreCase(screeningStatus) && visaStatus.contains("Valid") &&
            !"No baggage found for this flight".equals(baggageStatus)) {
            System.out.println("Passenger " + passengerId + " auto-approved for entry.");
            notifications.add("Passenger " + passengerId + " auto-approved for flight " + flightNumber);
        } else {
            System.out.println("Passenger not eligible for auto-approval. Check risk, visa, or baggage status.");
        }
    }

    private void batchProcessFlight() {
        String flightNumber = promptForFlightNumber();
        if (flightService != null && flightService.getFlightByNumber(flightNumber) != null) {
            System.out.println("\n=== Batch Processing for Flight " + flightNumber + " ===");
            String[] samplePassengers = {"P123", "P456", "P789"};
            for (String passengerId : samplePassengers) {
                displayPassengerInfo(passengerId, flightNumber);
                processedPassengers.add(passengerId + " (" + flightNumber + ")");
            }
            System.out.println("Batch processing completed for " + samplePassengers.length + " passengers.");
        } else {
            System.out.println("Flight not found or service unavailable.");
        }
        askToContinue();
    }

    private void viewSecurityQueueLength() {
        if (securityService != null) {
            int queueLength = securityService.getQueueLength();
            System.out.println("\nCurrent Security Queue Length: " + queueLength + " passengers");
            if (queueLength > 15) {
                System.out.println("Suggestion: Open additional checkpoint due to high queue length.");
            }
        } else {
            System.out.println("Security Check Service unavailable.");
        }
        askToContinue();
    }

    private void viewNotifications() {
        System.out.println("\n=== Notifications ===");
        if (notifications.isEmpty()) {
            System.out.println("No new notifications.");
        } else {
            for (String notification : notifications) {
                System.out.println("- " + notification);
            }
            notifications.clear();
        }
        System.out.println("=======================================");
        askToContinue();
    }

    private void generateProcessingReport() {
        System.out.println("\n=== Passenger Processing Report ===");
        if (processedPassengers.isEmpty()) {
            System.out.println("No passengers processed yet.");
        } else {
            System.out.println("Total Passengers Processed: " + processedPassengers.size());
            for (String passenger : processedPassengers) {
                System.out.println("- " + passenger);
            }
        }
        System.out.println("=======================================");
        askToContinue();
    }

    private void displayServiceStatus() {
        System.out.println("\n=== Service Availability ===");
        System.out.println("Security Check Service: " + (securityService != null ? "Available" : "Unavailable"));
        System.out.println("Passenger Check-In Service: " + (checkInService != null ? "Available" : "Unavailable"));
        System.out.println("Flight Schedule Service: " + (flightService != null ? "Available" : "Unavailable"));
        System.out.println("Passenger Profiling Service: " + (profilingService != null ? "Available" : "Unavailable"));
        System.out.println("Visa Verification Service: " + (visaService != null ? "Available" : "Unavailable"));
        System.out.println("Baggage Handling Service: " + (baggageService != null ? "Available" : "Unavailable"));
        System.out.println("=======================================");
        askToContinue();
    }

    private void displayHelp() {
        System.out.println("\n=== Help ===");
        System.out.println("This system manages customs and immigration with advanced features:");
        System.out.println("- Passenger ID: 'P' + 3 digits (e.g., P123)");
        System.out.println("- Flight Number: 'FL' + 3 digits (e.g., FL123)");
        System.out.println("- Seat: Number + letter (e.g., 12A)");
        System.out.println("- Screening Status: Waiting, Cleared, Denied");
        System.out.println("Features: Visa checks, baggage tracking, AI risk prediction, auto-approval.");
        System.out.println("=======================================");
        askToContinue();
    }

    private void askToContinue() {
        System.out.print("\nReturn to main menu? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        while (!response.equals("yes") && !response.equals("no")) {
            System.out.print("Please enter 'yes' or 'no': ");
            response = scanner.nextLine().trim().toLowerCase();
        }
        if ("no".equals(response)) {
            System.out.println("Goodbye!");
            stop();
            System.exit(0);
        }
    }

//    public static void main(String[] args) {
//        SecurityCheckService securityService = new securitycheckservice.SecurityCheckServiceImpl();
//        PassengerCheckInService checkInService = new passengercheckinservice.PassengerCheckInServiceImpl();
//        FlightScheduleService flightService = new com.osgi.flightsheduleservice.FlightScheduleServiceImpl();
//        PassengerProfilingService profilingService = new passengerprofilingservice.PassengerProfilingServiceImpl();
//        VisaVerificationService visaService = new visaverificationservice.VisaVerificationServiceImpl();
//        BaggageHandlingService baggageService = new baggagehandlingservice.BaggageHandlingServiceImpl();
//        CustomsImmigrationSystem system = new CustomsImmigrationSystem(securityService, checkInService, flightService,
//                profilingService, visaService, baggageService);
//        system.start();
//    }
}