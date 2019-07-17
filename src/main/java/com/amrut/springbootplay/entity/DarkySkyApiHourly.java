package com.amrut.springbootplay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkySkyApiHourly {

	@JsonProperty("summary")
	private String summary;

	@JsonProperty("time")
	private String time;


	@JsonProperty("precipProbability")
	private String precipProbability;
	
	@JsonProperty("cloudCover")
	private String cloudCover;
	

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPrecipProbability() {
		return precipProbability;
	}

	public void setPrecipProbability(String precipProbability) {
		this.precipProbability = precipProbability;
	}

	public String getCloudCover() {
		return cloudCover;
	}

	public void setCloudCover(String cloudCover) {
		this.cloudCover = cloudCover;
	}

}
