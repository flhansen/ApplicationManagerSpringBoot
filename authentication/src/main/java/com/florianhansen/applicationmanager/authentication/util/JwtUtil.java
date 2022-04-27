package com.florianhansen.applicationmanager.authentication.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.florianhansen.applicationmanager.authentication.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	@Value("${jwt.signkey}")
	private String SECRET_KEY;
	
	public String getUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	public Date getExpirationDate(String token) {
		return getClaim(token, Claims::getExpiration);
	}
	
	public Integer getUserId(String token) {
		return getClaim(token, new Function<Claims, Integer>() {

			@Override
			public Integer apply(Claims t) {
				return (Integer) t.get("userId");
			}

		});
	}
	
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims getAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return getExpirationDate(token)
				.before(new Date(System.currentTimeMillis()));
	}
	
	public String generateToken(UserDetails details) {
		JwtUser user = (JwtUser) details;
		return generateToken(user.getUsername(), user.getId());
	}
	
	public String generateToken(String username, int userId) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000)).claim("userId", userId)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token, UserDetails details) {
		final String username = getUsername(token);
		return username.equals(details.getUsername()) && !isTokenExpired(token);
	}
	
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
	
}
