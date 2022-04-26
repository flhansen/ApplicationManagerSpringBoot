package com.florianhansen.applicationmanager.loginservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.florianhansen.applicationmanager.crypto.HmacSHA256Encoder;
import com.florianhansen.applicationmanager.jwt.filter.JwtRequestFilter;
import com.florianhansen.applicationmanager.jwt.service.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@Import({
	com.florianhansen.applicationmanager.jwt.ModuleConfiguration.class,
	com.florianhansen.applicationmanager.model.ModuleConfiguration.class,
	com.florianhansen.applicationmanager.crypto.ModuleConfiguration.class})
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			// Don't require authentication for the login endpoint, so it's reachable.
			.authorizeRequests().antMatchers("/api/auth/login", "/api/auth/register").permitAll()
			// For the rest of the endpoints, authentication is required.
			.anyRequest().authenticated().and()
			// Dont use sessions, we want to use JsonWebTokens.
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// Place the JWT request filter just before the authentication filter.
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new HmacSHA256Encoder();
	}
	
}
