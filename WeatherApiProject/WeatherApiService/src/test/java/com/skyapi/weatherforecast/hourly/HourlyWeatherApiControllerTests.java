package com.skyapi.weatherforecast.hourly;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@SuppressWarnings({ "removal", "unused" })
@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherApiControllerTests {

	private static final String X_CURRENT_HOUR = "X-Current-Hour";

	private static final String END_POINT_PATH = "/v1/hourly";
	
	@Autowired private MockMvc mockMvc;
	@MockBean  private HourlyWeatherService hourlyWeatherService;
	@MockBean  private GeolocationService locationService;
	
	@Test
	public void testGetByIpShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
		mockMvc.perform(get(END_POINT_PATH))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	public void testGetByIpShouldReturn400BadRequestBecauseGeoLocationException() throws Exception {
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(GeoLocationException.class);
		
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "9"))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	public void testGetByIpShouldReturn204NoContent() throws Exception {
		
		int currentHour = 9;
		Location location = new Location().code("DELHI_IN");
		
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
			.andExpect(status().isNoContent())
			.andDo(print());
	}
	
	

	
	
	
}
