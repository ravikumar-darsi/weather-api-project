package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name= "weather_daily")
public class DailyWeather {
  
  @EmbeddedId
  private DailyWeatherId id  = new DailyWeatherId();

  private int minTemp;
  private int maxTemp;
  private int precipitation;

  @Column(length = 50)
  private String status;

  public DailyWeatherId getId() {
    return id;
  }

  public int getMinTemp() {
    return minTemp;
  }

  public int getMaxTemp() {
    return maxTemp;
  }

  public int getPrecipitation() {
    return precipitation;
  }

  public String getStatus() {
    return status;
  }


  
}
