package com.florianhansen.applicationmanager.applicationservice.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.florianhansen.applicationmanager.jwt.util.JwtUtil;
import com.florianhansen.applicationmananger.applicationservice.ApplicationServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationServiceApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class TypesControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private String token;
	
	@BeforeAll
	private void beforeAll() { 
		token = jwtUtil.generateToken("testuser", 0);
	}
	
	@Test
	public void getWorkTypesUnauthorized() throws Exception {
		mockMvc
			.perform(get("/api/types/worktypes"))
			.andExpect(status().is(200));
	}
	
	@Test
	public void getWorkTypesAuthorized() throws Exception {
		mockMvc
			.perform(get("/api/types/worktypes")
				.header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));
	}
	
	@Test
	public void getStatusesUnauthorized() throws Exception {
		mockMvc
			.perform(get("/api/types/statuses"))
			.andExpect(status().is(200));
	}
	
	@Test
	public void getStatusesAuthorized() throws Exception {
		mockMvc
			.perform(get("/api/types/statuses")
				.header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));

	}

}
