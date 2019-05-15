package com.amrut.springbootplay.entity;

public class CurrentForecast {
	private String currentSummary;
	private String daySummary;
	private String weekSummary;
	public String getCurrentSummary() {
		return currentSummary;
	}
	public void setCurrentSummary(String currentSummary) {
		this.currentSummary = currentSummary;
	}
	public String getDaySummary() {
		return daySummary;
	}
	public void setDaySummary(String daySummary) {
		this.daySummary = daySummary;
	}
	public String getWeekSummary() {
		return weekSummary;
	}
	public void setWeekSummary(String weekSummary) {
		this.weekSummary = weekSummary;
	}
	@Override
	public String toString() {
		return "CurrentForecast [currentSummary=" + currentSummary + ", daySummary=" + daySummary + ", weekSummary="
				+ weekSummary + "]";
	}

	
}
