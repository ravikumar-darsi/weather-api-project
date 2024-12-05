package com.skyapi.weatherforecast.hourly;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {

	private HourlyWeatherService hourlyWeatherService;
	private GeolocationService geolocationService;

	public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService,
			GeolocationService geolocationService) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.geolocationService = geolocationService;
	}

	@GetMapping
	public ResponseEntity<?> listHourlyForecastsByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIpAddress(request);

		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

			Location locationFromIP = geolocationService.getLocation(ipAddress);

			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);

			if (hourlyForecast == null || hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(hourlyForecast);
		} catch (NumberFormatException |  GeoLocationException ex) {
			return ResponseEntity.badRequest().build();
		} catch (LocationNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

}
