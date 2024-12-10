package com.skyapi.weatherforecast.daily;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyWeatherListDTO {
	
	private String location;
	
	@JsonProperty("daily_forecast")
	private List<DailyWeatherDTO> dailyForecast = new ArrayList<>();

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<DailyWeatherDTO> getDailyForecast() {
		return dailyForecast;
	}

	public void setDailyForecast(List<DailyWeatherDTO> dailyForecast) {
		this.dailyForecast = dailyForecast;
	}
	
	public void addDailyWeatherDTO(DailyWeatherDTO dto) {
		this.dailyForecast.add(dto);
	}
}
