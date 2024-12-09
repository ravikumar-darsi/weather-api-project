package com.skyapi.weatherforecast.common;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@SuppressWarnings("serial")
@Embeddable
public class DailyWeatherId implements Serializable {

  private int datOfMonth;
  private int month;

  @ManyToOne
  @JoinColumn(name= "location_code")
  private Location location;

  public DailyWeatherId() {
    
  }

  public DailyWeatherId(int datOfMonth, int month, Location location) {
    this.datOfMonth = datOfMonth;
    this.month = month;
    this.location = location;
  }

  public int getDatOfMonth() {
    return datOfMonth;
  }

  public void setDatOfMonth(int datOfMonth) {
    this.datOfMonth = datOfMonth;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

}
