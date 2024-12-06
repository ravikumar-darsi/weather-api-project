package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class HourlyWeatherService {

	private HourlyWeatherRepository hourlyWeatherRepo;
	private LocationRepository locationRepo;

	public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepo, LocationRepository locationRepo) {
		super();
		this.hourlyWeatherRepo = hourlyWeatherRepo;
		this.locationRepo = locationRepo;
	}

	public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given country code and city name");
		}
		return hourlyWeatherRepo.findByLocationCode(locationInDB.getCode(), currentHour);
	}

	public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour)
			throws LocationNotFoundException {
		Location locationInDB = locationRepo.findByCode(locationCode);

		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given code" + locationCode);
		}
		return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);
	}

	public List<HourlyWeather> updateByLocationCode(String locationCode,
			List<HourlyWeather> hourlyWeatherInRequest)throws LocationNotFoundException {
		
		Location location = locationRepo.findByCode(locationCode);

		if (location == null) {
			
			throw new LocationNotFoundException("No location found with the given code" + locationCode);
		}
		
		for (HourlyWeather item : hourlyWeatherInRequest) {
			item.getId().setLocation(location);
		}
		
		List<HourlyWeather> hourlyWeatherInDB = location.getListHourlyWeather();
		List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();
		
		for (HourlyWeather item : hourlyWeatherInDB) {
			if ( !hourlyWeatherInRequest.contains(item )) {
				hourlyWeatherToBeRemoved.add(item.getShallowCopy());
			}
		}
		
		for (HourlyWeather item : hourlyWeatherToBeRemoved) {
			hourlyWeatherInDB.remove(item);
		}
		
		return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherInRequest);
	}
}
