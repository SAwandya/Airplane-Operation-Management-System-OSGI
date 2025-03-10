package visaverificationservice;

public interface VisaVerificationService {
    String checkVisaStatus(String passengerId, String destination);
    boolean isVisaRequired(String destination);
}