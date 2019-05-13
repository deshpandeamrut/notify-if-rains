package com.amrut.springbootplay.entity;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Source {

	@JsonProperty("id")
	private String sourceId;
	
	@JsonProperty("name")
	private String sourceName;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("url")
	private String url;

	@JsonProperty("category")
	private String category;
	
	@JsonProperty("language")
	private String language;
	
	@JsonProperty("country")
	private String country;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
