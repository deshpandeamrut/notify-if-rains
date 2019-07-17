package com.amrut.springbootplay.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkySkyApiHourlyOverview {

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("data")
	private List<DarkySkyApiHourly> hourlyData;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<DarkySkyApiHourly> getHourlyData() {
		return hourlyData;
	}

	public void setHourlyData(List<DarkySkyApiHourly> hourlyData) {
		this.hourlyData = hourlyData;
	}

}
