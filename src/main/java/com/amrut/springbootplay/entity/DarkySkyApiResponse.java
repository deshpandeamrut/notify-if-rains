package com.amrut.springbootplay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkySkyApiResponse {

	@JsonProperty("daily")
	private DarkySkyApiDaily darkySkyApiDaily;

	@JsonProperty("currently")
	private DarkySkyApiCurrently darkySkyApiCurrently;

	@JsonProperty("hourly")
	private DarkySkyApiHourlyOverview darkySkyApiHourly;
	
	public DarkySkyApiDaily getDarkySkyApiDaily() {
		return darkySkyApiDaily;
	}

	public void setDarkySkyApiDaily(DarkySkyApiDaily darkySkyApiDaily) {
		this.darkySkyApiDaily = darkySkyApiDaily;
	}

	public DarkySkyApiCurrently getDarkySkyApiCurrently() {
		return darkySkyApiCurrently;
	}

	public void setDarkySkyApiCurrently(DarkySkyApiCurrently darkySkyApiCurrently) {
		this.darkySkyApiCurrently = darkySkyApiCurrently;
	}

	public DarkySkyApiHourlyOverview getDarkySkyApiHourly() {
		return darkySkyApiHourly;
	}

	public void setDarkySkyApiHourly(DarkySkyApiHourlyOverview darkySkyApiHourly) {
		this.darkySkyApiHourly = darkySkyApiHourly;
	}
}
