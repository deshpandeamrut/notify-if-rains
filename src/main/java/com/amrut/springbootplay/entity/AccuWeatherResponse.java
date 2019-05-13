package com.amrut.springbootplay.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccuWeatherResponse {

	@JsonProperty("Headline")
	private AccuWeatherHeadline headline;

	@JsonProperty("DailyForecasts")
	private List<AccuWeatherForecasts> forecasts;

	public AccuWeatherHeadline getHeadline() {
		return headline;
	}

	public void setHeadline(AccuWeatherHeadline headline) {
		this.headline = headline;
	}

	public List<AccuWeatherForecasts> getForecasts() {
		return forecasts;
	}

	public void setForecasts(List<AccuWeatherForecasts> forecasts) {
		this.forecasts = forecasts;
	}
	


	
}
