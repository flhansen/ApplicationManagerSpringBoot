package com.florianhansen.applicationmanager.loginservice.model;

public abstract class ApiResponse {
	
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
