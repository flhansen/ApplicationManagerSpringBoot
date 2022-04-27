package com.florianhansen.applicationmanager.loginservice;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.florianhansen.applicationmanager.crypto.HmacSHA256Encoder;
import com.florianhansen.applicationmanager.jwt.service.JwtAuthenticationFacade;
import com.florianhansen.applicationmanager.jwt.service.JwtUserDetailsService;
import com.florianhansen.applicationmanager.jwt.util.JwtUtil;
import com.florianhansen.applicationmanager.loginservice.model.AuthenticationRequest;
import com.florianhansen.applicationmanager.loginservice.model.AuthenticationResponse;
import com.florianhansen.applicationmanager.loginservice.model.DeleteResponse;
import com.florianhansen.applicationmanager.loginservice.model.RegisterRequest;
import com.florianhansen.applicationmanager.loginservice.model.RegisterResponse;
import com.florianhansen.applicationmanager.model.Account;
import com.florianhansen.applicationmanager.model.repository.AccountRepository;

@CrossOrigin
@RestController
@RequestMapping("api/auth")
public class AuthController {
	
	@Autowired
	private JwtAuthenticationFacade jwtFacade;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private HmacSHA256Encoder passwordEncoder;
	
	@Autowired
	private AccountRepository accountRepo;

	@PostMapping("login")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) throws Exception {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect username or password", e);
		}
		
		final UserDetails details = userDetailsService.loadUserByUsername(request.getUsername());
		final String token = jwtUtil.generateToken(details);

		return ResponseEntity.ok(new AuthenticationResponse(HttpStatus.OK, "Login successful", token));
	}
	
	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		String passwordHash = passwordEncoder.encode(request.getPassword());
		
		Account account = new Account();
		account.setUsername(request.getUsername());
		account.setPassword(passwordHash);
		account.setEmail(request.getEmail());
		account.setCreationDate(new Date(System.currentTimeMillis()));
		
		List<Account> accounts = accountRepo.findAll();
		accounts.add(account);
		
		try {
			accountRepo.saveAll(accounts);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already exists", e);
		}
		
		return ResponseEntity.ok(new RegisterResponse(HttpStatus.OK, "User has been registered"));
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<?> delete() {
		String token = jwtFacade.getToken();
		String username = jwtUtil.getUsername(token);
		
		try {
			Account account = accountRepo.findAccountByUsername(username);
			accountRepo.delete(account);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
		
		return ResponseEntity.ok(new DeleteResponse(HttpStatus.OK, "User '" + username + "' has been deleted"));
	}
	
	@ExceptionHandler(value = UsernameNotFoundException.class)
	public void exeptionHandler() {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found or credentials wrong");
	}
}
