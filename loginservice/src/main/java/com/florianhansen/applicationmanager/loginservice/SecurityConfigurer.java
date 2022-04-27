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

import com.florianhansen.applicationmanager.authentication.filter.JwtRequestFilter;
import com.florianhansen.applicationmanager.authentication.service.JwtUserDetailsService;
import com.florianhansen.applicationmanager.cryptography.HmacSHA256Encoder;

@Configuration
@EnableWebSecurity
@Import({
	com.florianhansen.applicationmanager.authentication.ModuleConfiguration.class,
	com.florianhansen.applicationmanager.model.ModuleConfiguration.class,
	com.florianhansen.applicationmanager.cryptography.ModuleConfiguration.class})
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
			.authorizeRequests().antMatchers("/api/auth/login", "/api/auth/register").permitAll()
			.anyRequest().authenticated().and()
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
