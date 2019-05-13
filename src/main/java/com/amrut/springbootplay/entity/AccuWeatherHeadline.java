package com.amrut.springbootplay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccuWeatherHeadline {
	
	@JsonProperty("Text")
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}
