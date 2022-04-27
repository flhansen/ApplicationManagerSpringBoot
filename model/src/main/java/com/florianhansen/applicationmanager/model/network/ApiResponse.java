package com.florianhansen.applicationmanager.model.network;

import org.springframework.http.HttpStatus;

public abstract class ApiResponse {
	
	private HttpStatus status;
	private String message;
	
	public ApiResponse(HttpStatus status, String message) {
		setStatus(status);
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status.value();
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}
