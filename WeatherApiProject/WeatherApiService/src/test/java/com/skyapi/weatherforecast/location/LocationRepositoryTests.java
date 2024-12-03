package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

	@Autowired
	private LocationRepository repository;

	@Test
	public void testAddSuccess() {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		Location savedLocation = repository.save(location);

		assertThat(savedLocation).isNotNull();
		assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
	}

	@Test
	public void testListSuccess() {
		List<Location> locations = repository.findUntrashed();

		assertThat(locations).isNotEmpty();

		locations.forEach(System.out::println);
	}

	@Test
	public void testGetNotFound() {
		String code = "ABCD";
		Location location = repository.findByCode(code);

		assertThat(location).isNull();
	}

	@Test
	public void testGetFound() {
		String code = "LACA_USA";
		Location location = repository.findByCode(code);

		assertThat(location).isNotNull();
		assertThat(location.getCode()).isEqualTo(code);
	}

	@Test
	public void testTrashSuccess() {
		String code = "LACA_USA";
		repository.trashByCode(code);

		Location location = repository.findByCode(code);

		assertThat(location).isNull();
	}

	@Test
	public void testAddRealtimeWeatherData() {
		String code = "DELHI_IN";

		Location location = repository.findByCode(code);

		RealtimeWeather realtimeWeather = location.getRealtimeWeather();

		if (realtimeWeather == null) {
			realtimeWeather = new RealtimeWeather();
			realtimeWeather.setLocation(location);
			location.setRealtimeWeather(realtimeWeather);
		}

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(50);
		realtimeWeather.setPrecipitation(20);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(15);
		realtimeWeather.setLastUpdated(new Date());

		Location updatedLocation = repository.save(location);

		assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
	}
}
