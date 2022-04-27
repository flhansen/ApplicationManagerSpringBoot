package com.florianhansen.applicationmanager.loginservice.model;

import org.springframework.http.HttpStatus;

import com.florianhansen.applicationmanager.model.network.ApiResponse;

public class AuthenticationResponse extends ApiResponse {

	private final String token;
	
	public AuthenticationResponse(HttpStatus status, String message, String token) {
		super(status, message);

		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
}
