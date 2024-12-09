package rest.client.examples.realtime.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherByIPAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String clientIpAddress = "103.48.198.141";	// Delhi, India
		headers.add("X-FORWARDED-FOR", clientIpAddress);
		
		HttpEntity<String> request = new HttpEntity<>(headers);
		
		var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println(response.getBody());
		}
	}

}
