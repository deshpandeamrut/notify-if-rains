package com.amrut.springbootplay.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkySkyApiDaily {

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("data")
	private List<DarkySkyApiWeek> weekData;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<DarkySkyApiWeek> getWeekData() {
		return weekData;
	}

	public void setWeekData(List<DarkySkyApiWeek> weekData) {
		this.weekData = weekData;
	}



	
}
