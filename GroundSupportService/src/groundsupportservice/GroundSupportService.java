package groundsupportservice;

public interface GroundSupportService {
	
	String getGroundSupportStatus(String flightNumber);
    void updateGroundSupportStatus(String flightNumber, String status);

}
