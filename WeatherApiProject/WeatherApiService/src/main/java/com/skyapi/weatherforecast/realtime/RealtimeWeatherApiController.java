package com.skyapi.weatherforecast.realtime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {

	private static Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);

	private GeolocationService locationService;
	private RealtimeWeatherService realtimeWeatherService;
	private ModelMapper modelMapper;

	public RealtimeWeatherApiController(GeolocationService locationService,
			RealtimeWeatherService realtimeWeatherService, ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.realtimeWeatherService = realtimeWeatherService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> getRealtimWeatherByIPAdddress(HttpServletRequest request) {
		String ipAddress = CommonUtility.getIpAddress(request);

		try {
			Location locationFromIP = locationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);

			RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

			return ResponseEntity.ok(dto);

		} catch (GeoLocationException e) {
			LOGGER.error(ipAddress);

			return ResponseEntity.badRequest().build();

		} 
	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {

			RealtimeWeather realtimeWeather = realtimeWeatherService.getLocationCode(locationCode);
			// RealtimeWeatherDTO dto = modelMapper.map(realtimeWeather,
			// RealtimeWeatherDTO.class);

			return ResponseEntity.ok(entity2DTO(realtimeWeather));

	}

	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode,
			@RequestBody @Valid RealtimeWeather realtimeWeatherInRequest) {
		
			realtimeWeatherInRequest.setLocationCode(locationCode);

			RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode,
					realtimeWeatherInRequest);

			// RealtimeWeatherDTO dto = modelMapper.map(updatedRealtimeWeather,
			// RealtimeWeatherDTO.class);

			return ResponseEntity.ok(entity2DTO(updatedRealtimeWeather));

	}

	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);

	}

}
