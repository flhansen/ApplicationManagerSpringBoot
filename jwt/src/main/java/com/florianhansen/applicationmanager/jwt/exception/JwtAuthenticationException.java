package com.florianhansen.applicationmanager.jwt.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = -8687812228039128978L;

	public JwtAuthenticationException(String msg) {
		super(msg);
	}

}
