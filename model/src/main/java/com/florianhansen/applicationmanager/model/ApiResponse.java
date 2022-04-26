package com.florianhansen.applicationmanager.model;

public class ApiResponse {
	
	private String message;
	
	public ApiResponse(String message) {
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
