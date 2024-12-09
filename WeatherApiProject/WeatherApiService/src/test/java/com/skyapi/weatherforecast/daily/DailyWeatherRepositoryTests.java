package com.skyapi.weatherforecast.daily;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DailyWeatherRepositoryTests {

	@Autowired
	private DailyWeatherRepository repository;
	
	@Test
	public void testAdd() {
		String locationCode= "DANA_VN";
		
		Location location = new Location().code(locationCode);
		
		
		DailyWeather forecast = new DailyWeather()
						.location(location)
						.dayOfMonth(16)
						.month(7)
						.minTemp(23)
						.maxTemp(32)
						.precipitation(40)
						.status("Cloudy");
		
		DailyWeather addedForecast = repository.save(forecast);
		
		assertThat(addedForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
		
	}
	
	@Test
	public void testDelete() {
		String locationCode= "DELHI_IN";
		
		Location location = new Location().code(locationCode);
		
		DailyWeatherId id = new DailyWeatherId(16, 7, location);
		
		repository.deleteById(id);
		
		Optional<DailyWeather> result = repository.findById(id);
		
		assertThat(result).isNotPresent();
	}
}
