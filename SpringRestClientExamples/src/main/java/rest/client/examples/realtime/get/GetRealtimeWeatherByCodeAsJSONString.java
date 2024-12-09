package rest.client.examples.realtime.get;

import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherByCodeAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		String locationCode = "DELHI_IN";
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(requestURI, String.class, locationCode);
		
		System.out.println(response);
	}

}
