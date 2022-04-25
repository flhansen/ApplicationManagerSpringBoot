package com.florianhansen.applicationmananger.applicationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.florianhansen.applicationmanager.jwt.filter.JwtAuthFilter;
import com.florianhansen.applicationmanager.jwt.service.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
@Import({
	com.florianhansen.applicationmanager.model.ModuleConfiguration.class,
	com.florianhansen.applicationmanager.jwt.ModuleConfiguration.class})
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationProvider authenticationProvider;
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
			.anyRequest().authenticated();
		
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
}
