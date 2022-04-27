package com.florianhansen.applicationmanager.authentication.service;

import org.springframework.security.core.Authentication;

public interface TokenAuthenticationFacade {

	Authentication getAuthentication();
	String getToken();
	
}
