package com.amrut.springbootplay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccuWeatherForecasts {

	@JsonProperty("Date")
	private String Date;
	
	@JsonProperty("Day")
	private AccuWeatherForecatsDay Day;
	
	@JsonProperty("Night")
	private AccuWeatherForecatsNight Night;

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public AccuWeatherForecatsDay getDay() {
		return Day;
	}

	public void setDay(AccuWeatherForecatsDay day) {
		Day = day;
	}

	public AccuWeatherForecatsNight getNight() {
		return Night;
	}

	public void setNight(AccuWeatherForecatsNight night) {
		Night = night;
	}
	
	
	
	
}
