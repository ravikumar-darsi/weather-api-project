package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {

	@Autowired
	private RealtimeWeatherRepository repo;
	
	@Test
	public void testUpdate() {
		String locationCode = "NYC_USA";

		RealtimeWeather realtimeWeather = repo.findById(locationCode).get();
		
		realtimeWeather.setTemperature(-2);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(42);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindSpeed(12);
		realtimeWeather.setLastUpdated(new Date());
		
		RealtimeWeather updatedRealtimeWeather = repo.save(realtimeWeather);
		
		assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(32);
	}
	
	@Test
	public void testFindByCountryCodeAndCityNotFound() {
		String countryCode ="JP";
		String cityName = "Tokyo";
		
		RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, cityName);
		
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByCountryCodeAndCityFound() {
		String countryCode ="US";
		String cityName = "New York City";
		
		RealtimeWeather realtimeWeather = repo.findByCountryCodeAndCity(countryCode, cityName);
		
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(cityName);
	}
	
	
	@Test
	public void testFindByLocationNotFound() {
		String loccationCode = "ABCXYZ";
		
		RealtimeWeather realtimeWeather = repo.findByLocationCode(loccationCode);
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByTrashedNotFound() {
		String loccationCode = "NYC_USA";
		
		RealtimeWeather realtimeWeather = repo.findByLocationCode(loccationCode);
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByLocationFound() {
		String loccationCode = "DELHI_In";
		
		RealtimeWeather realtimeWeather = repo.findByLocationCode(loccationCode);
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocationCode()).isEqualTo(loccationCode);
	}
}
