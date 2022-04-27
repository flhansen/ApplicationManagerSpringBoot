package com.florianhansen.applicationmanager.model.network;

import org.springframework.http.HttpStatus;

public class OkResponse extends ApiResponse {

	public OkResponse(String message) {
		super(HttpStatus.OK, message);
	}

}
