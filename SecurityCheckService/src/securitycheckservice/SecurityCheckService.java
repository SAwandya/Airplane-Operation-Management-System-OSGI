package securitycheckservice;

public interface SecurityCheckService {
	
	String getScreeningStatus(String passengerId);
    int getQueueLength();
    void updateScreeningStatus(String passengerId, String status);

}
