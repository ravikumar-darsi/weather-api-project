package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.BadRequestException;
import com.skyapi.weatherforecast.CommonUtility;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hourly")
@Validated
public class HourlyWeatherApiController {

	private HourlyWeatherService hourlyWeatherService;
	private GeolocationService geolocationService;
	private ModelMapper modelMapper;
	
	

	public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService, GeolocationService geolocationService,
			ModelMapper modelMapper) {
		super();
		this.hourlyWeatherService = hourlyWeatherService;
		this.geolocationService = geolocationService;
		this.modelMapper = modelMapper;
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
			return ResponseEntity.ok(listEntity2DTO(hourlyForecast));
			
		} catch (NumberFormatException |  GeoLocationException ex) {
			
			return ResponseEntity.badRequest().build();
			
		} catch (LocationNotFoundException ex) {
			
			return ResponseEntity.notFound().build();
			
		}
	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyForecastByLocationCode(
		 @PathVariable("locationCode") String locationCode, HttpServletRequest request	){
		try {
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
			List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
			if(hourlyForecast.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
				return ResponseEntity.ok(listEntity2DTO(hourlyForecast));
		
		}catch (NumberFormatException ex) {
				return ResponseEntity.badRequest().build();		
		} catch (LocationNotFoundException ex) {
				return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String locationCode,
							@RequestBody @Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException{
		if (listDTO.isEmpty()) {
			throw new BadRequestException("Hourly forecast data cannot be empty");
		}
		
		listDTO.forEach(System.out::println);
		
		List<HourlyWeather> listHourlyWeather = listDTO2ListEntity(listDTO);
		
		System.out.println(); 
		
		listHourlyWeather.forEach(System.out::println);
		
		try {
			
			List<HourlyWeather> updatedHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, listHourlyWeather);
			return ResponseEntity.ok(listEntity2DTO(updatedHourlyWeather));
		
		} catch (LocationNotFoundException e) {
			
			return ResponseEntity.notFound().build();
		}
		
	}
	
	private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO){
		
		List<HourlyWeather> listEntity = new ArrayList<>();
		listDTO.forEach(dto -> {
			listEntity.add(modelMapper.map(dto, HourlyWeather.class));
		});
		return listEntity;
	}
	
	private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyForecast) {
		Location location = hourlyForecast.get(0).getId().getLocation();
		
		HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
		listDTO.setLocation(location.toString());
		
		hourlyForecast.forEach(hourlyWeather -> {
			HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
			listDTO.addWeatherHourlyDTO(dto);
		});
		
		return listDTO;
	}
}
