package weatherairtrafficservice;

public interface WeatherAirTrafficService {

	String getWeatherReport();
    String getFlightPathAdjustment(String flightNumber);
}
