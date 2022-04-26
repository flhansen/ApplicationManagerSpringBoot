package com.florianhansen.applicationmanager.loginservice;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florianhansen.applicationmanager.crypto.HmacSHA256Encoder;
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
			throw new Exception("Incorrect username or password", e);
		}
		
		final UserDetails details = userDetailsService.loadUserByUsername(request.getUsername());
		final String token = jwtUtil.generateToken(details);

		return ResponseEntity.ok(new AuthenticationResponse(token));
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
		accountRepo.saveAll(accounts);
		
		return ResponseEntity.ok(new RegisterResponse("User has been registered"));
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<?> delete(Principal principal) {
		String username = principal.getName();
		
		Account account = accountRepo.findAccountByUsername(username);
		accountRepo.delete(account);
		
		return ResponseEntity.ok(new DeleteResponse("User '" + username + "' has been deleted"));
	}
	
}
