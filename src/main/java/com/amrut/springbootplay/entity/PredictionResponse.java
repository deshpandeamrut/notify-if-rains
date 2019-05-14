package com.amrut.springbootplay.entity;

public class PredictionResponse {

	private String message;
	
	private String source;
	
	private String actualMessage;
	
	

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
