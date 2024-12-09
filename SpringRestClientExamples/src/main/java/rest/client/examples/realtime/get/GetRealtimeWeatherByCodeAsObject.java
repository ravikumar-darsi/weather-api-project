package rest.client.examples.realtime.get;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.realtime.RealtimeWeather;

public class GetRealtimeWeatherByCodeAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/DELHI_IN";
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			RealtimeWeather response = restTemplate.getForObject(requestURI, RealtimeWeather.class);
			
			System.out.println(response);
		} catch (RestClientResponseException ex) {
			System.out.println("Response error code: " + ex.getStatusCode());
			ex.printStackTrace();
		}
	}

}
