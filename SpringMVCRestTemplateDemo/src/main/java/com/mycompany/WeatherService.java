package com.mycompany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

	@Value("${api.weather.realtime.get.uri}")
	private String getRealtimeWeatherRequestURI;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public RealtimeWeather getRealtimeWeather() throws WeatherServiceException {
		try {
			return restTemplate.getForObject(getRealtimeWeatherRequestURI, RealtimeWeather.class);
		}catch (RestClientException ex) {
			String message = "Error calling Get Realtime Weather API: " + ex.getMessage();
			ex.printStackTrace();
			throw new WeatherServiceException(message);
		}
	}
}
