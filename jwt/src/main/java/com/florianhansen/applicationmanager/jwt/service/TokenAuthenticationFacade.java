package com.florianhansen.applicationmanager.jwt.service;

import org.springframework.security.core.Authentication;

public interface TokenAuthenticationFacade {

	Authentication getAuthentication();
	String getToken();
	
}
