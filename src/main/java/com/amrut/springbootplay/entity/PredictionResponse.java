package com.amrut.springbootplay.entity;

public class PredictionResponse {

	private String message;
	
	private String source;
	
	private String actualMessage;
	
	private String hours;
	
	private String precProbab;

	public String getActualMessage() {
		return actualMessage;
	}

	public void setActualMessage(String actualMessage) {
		this.actualMessage = actualMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
	
	public String getHours() {
		return hours;
	}

	public void setHours(String string) {
		this.hours = string;
	}

	
	public String getPrecProbab() {
		return precProbab;
	}

	public void setPrecProbab(String precProbab) {
		this.precProbab = precProbab;
	}

	public PredictionResponse() {
		super();
		this.message = "NA";
		this.source = "";
		this.actualMessage = "";
	}

	@Override
	public String toString() {
		return "PredictionResponse [message=" + message + ", source=" + source + "]";
	}
	
	
}
