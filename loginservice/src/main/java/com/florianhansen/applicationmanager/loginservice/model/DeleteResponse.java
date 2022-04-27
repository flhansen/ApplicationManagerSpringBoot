package com.florianhansen.applicationmanager.loginservice.model;

import org.springframework.http.HttpStatus;

import com.florianhansen.applicationmanager.model.network.ApiResponse;

public class DeleteResponse extends ApiResponse {

	public DeleteResponse(HttpStatus status, String message) {
		super(status, message);
	}

}
