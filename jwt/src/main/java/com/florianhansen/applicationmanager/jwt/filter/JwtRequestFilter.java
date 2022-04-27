package com.florianhansen.applicationmanager.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.florianhansen.applicationmanager.jwt.service.JwtUserDetailsService;
import com.florianhansen.applicationmanager.jwt.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		
		if (authorizationHeader == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String[] authorizationHeaderTokens = authorizationHeader.split(" ");
		
		if (authorizationHeaderTokens.length != 2) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!authorizationHeaderTokens[0].equalsIgnoreCase("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeaderTokens[1];
		String username = jwtUtil.getUsername(token);
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails details = userDetailsService.loadUserByUsername(username);
			
			if (details != null && jwtUtil.validateToken(token, details)) {
				UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
				usernamePasswordToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
