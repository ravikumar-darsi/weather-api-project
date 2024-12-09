package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;

public class AddLocationAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";

		RestTemplate restTemplate = new RestTemplate();

		Location newLocation = new Location();
		newLocation.setCode("BNGL_IN");
		newLocation.setCityName("Bangalore");
		newLocation.setRegionName("Karnataka");
		newLocation.setCountryCode("IN");
		newLocation.setCountryName("India");

		HttpEntity<Location> request = new HttpEntity<>(newLocation);

		ResponseEntity<Location> response = restTemplate.postForEntity(requestURI, request, Location.class);

		HttpStatusCode statusCode = response.getStatusCode();

		System.out.println("Response status code: " + statusCode);

		Location addedLocation = response.getBody();

		System.out.println(addedLocation);

	}

}
