package com.amrut.springbootplay.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkySkyApiDaily {

	@JsonProperty("summary")
	private String string;

	@JsonProperty("data")
	private List<DarkySkyApiWeek> weekData;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public List<DarkySkyApiWeek> getWeekData() {
		return weekData;
	}

	public void setWeekData(List<DarkySkyApiWeek> weekData) {
		this.weekData = weekData;
	}



	
}
