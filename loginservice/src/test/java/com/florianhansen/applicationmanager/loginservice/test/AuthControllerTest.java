package com.florianhansen.applicationmanager.loginservice.test;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.florianhansen.applicationmanager.crypto.HmacSHA256Encoder;
import com.florianhansen.applicationmanager.jwt.util.JwtUtil;
import com.florianhansen.applicationmanager.loginservice.model.AuthenticationRequest;
import com.florianhansen.applicationmanager.loginservice.model.RegisterRequest;
import com.florianhansen.applicationmanager.model.Account;
import com.florianhansen.applicationmanager.model.repository.AccountRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private HmacSHA256Encoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Test
	public void testLoginBadRequest() throws Exception {
		mockMvc
			.perform(post("/api/auth/login"))
			.andExpect(status().is(400));
	}

	@Test
	public void testLoginWrongCredentials() throws Exception {
		Account acc = new Account();
		acc.setUsername("testuser");
		acc.setPassword(passwordEncoder.encode("undecryptable"));
		acc.setEmail("test@test.test");
		acc.setCreationDate(new Date());
		accountRepo.save(acc);
		
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername("testuser");
		request.setPassword("und3cryptable");
		
		mockMvc
			.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().is(400));
	}
	
	@Test
	public void testLogin() throws Exception {
		Account acc = new Account();
		acc.setUsername("testuser");
		acc.setPassword(passwordEncoder.encode("undecryptable"));
		acc.setEmail("test@test.test");
		acc.setCreationDate(new Date());
		accountRepo.save(acc);
		
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername("testuser");
		request.setPassword("undecryptable");
		
		mockMvc
			.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().is(200));
	}

	@Test
	public void testRegisterBadRequest() throws Exception {
		mockMvc
			.perform(post("/api/auth/register"))
			.andExpect(status().is(400));
	}

	@Test
	public void testRegisterUserExists() throws Exception {
		Account acc = new Account();
		acc.setUsername("testuser");
		acc.setPassword(passwordEncoder.encode("undecryptable"));
		acc.setEmail("test@test.test");
		acc.setCreationDate(new Date());
		accountRepo.save(acc);
		
		RegisterRequest request = new RegisterRequest();
		request.setUsername("testuser");
		request.setPassword("randompwd");
		request.setEmail("test@test.test");
		
		mockMvc
			.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().is(400));
	}
	
	@Test
	public void testRegister() throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("testuser");
		request.setPassword(passwordEncoder.encode("randompwd"));
		request.setEmail("test@test.test");
		
		mockMvc
			.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().is(200));
	}

	@Test
	public void testDeleteUnauthorized() throws Exception {
		mockMvc
			.perform(delete("/api/auth/delete"))
			.andExpect(status().is(403));
	}
	
	@Test
	public void testDeleteUserDoesNotExist() throws Exception {
		String token = jwtUtil.generateToken("testuser", 0);
		
		mockMvc
			.perform(delete("/api/auth/delete")
				.header("Authorization", "Bearer " + token))
			.andExpect(status().is(403));
	}

	@Test
	public void testDelete() throws Exception {
		Account acc = new Account();
		acc.setUsername("testuser");
		acc.setPassword(passwordEncoder.encode("undecryptable"));
		acc.setEmail("test@test.test");
		acc.setCreationDate(new Date());
		acc = accountRepo.save(acc);

		String token = jwtUtil.generateToken(acc.getUsername(), acc.getId());
		
		mockMvc
			.perform(delete("/api/auth/delete")
				.header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));
	}

}
