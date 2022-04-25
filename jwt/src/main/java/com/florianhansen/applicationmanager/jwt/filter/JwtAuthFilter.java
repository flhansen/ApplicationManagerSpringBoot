package com.florianhansen.applicationmanager.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.florianhansen.applicationmanager.jwt.JwtAuthentication;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO: See https://ertan-toker.de/spring-boot-spring-security-jwt-token-en/
		final String authorizationHeader = request.getHeader("Authorization");
		
		if (authorizationHeader == null) {
			filterChain.doFilter(request, response);
			return;
		}

		String[] authHeaderParts = authorizationHeader.split(" ");
		
		if (authHeaderParts.length != 2) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!authHeaderParts[0].equalsIgnoreCase("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String token = authHeaderParts[1];
		JwtAuthentication authentication = new JwtAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		filterChain.doFilter(request, response);
	}

}
