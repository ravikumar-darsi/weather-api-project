package rest.client.examples.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HourlyWeather {
	@JsonProperty("hour_of_day")
	private int hourOfDay;
	private int temperature;
	private int precipitation;
	
	private String status;

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "HourlyWeather [hourOfDay=" + hourOfDay + ", temperature=" + temperature + ", precipitation="
				+ precipitation + ", status=" + status + "]";
	}
	
	
}
