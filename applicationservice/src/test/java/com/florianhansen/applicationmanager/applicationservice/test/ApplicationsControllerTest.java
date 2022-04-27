package com.florianhansen.applicationmanager.applicationservice.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import com.florianhansen.applicationmanager.authentication.util.JwtUtil;
import com.florianhansen.applicationmanager.model.Application;
import com.florianhansen.applicationmanager.model.repository.ApplicationRepository;
import com.florianhansen.applicationmananger.applicationservice.ApplicationServiceApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationServiceApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ApplicationsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ApplicationRepository applicationRepo;
	
	private String token;
	
	@BeforeAll
	public void beforeAll() {
		token = jwtUtil.generateToken("testuser", 0);
	}
	
	@Test
	public void testGetApplicationsUnauthorized() throws Exception {
		mockMvc
			.perform(get("/api/applications"))
			.andExpect(status().is(403));
	}
	
	@Test
	public void testGetApplicationsAuthorized() throws Exception {
		mockMvc
			.perform(get("/api/applications").header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));
	}
	
	@Test
	public void testGetApplicationUnauthorized() throws Exception {
		mockMvc
			.perform(get("/api/applications/1"))
			.andExpect(status().is(403));
	}
	
	@Test
	public void testGetApplicationNotFound() throws Exception {
		mockMvc
			.perform(get("/api/applications/1").header("Authorization", "Bearer " + token))
			.andExpect(status().is(404));
	}
	
	@Test
	public void testGetApplicationAuthorized() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);
		
		mockMvc
			.perform(get("/api/applications/" + app1.getId()).header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));
	}
	
	@Test
	public void testCreateApplicationUnauthorized() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		
		mockMvc
			.perform(post("/api/applications").content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(403));
	}

	@Test
	public void testCreateApplicationAuthorized() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		
		mockMvc
			.perform(post("/api/applications")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(200));
	}
	
	@Test
	public void testDeleteApplicationNotFound() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		applicationRepo.save(app1);
		
		mockMvc
			.perform(delete("/api/applications/200").header("Authorization", "Bearer " + token))
			.andExpect(status().is(404));
	}
	
	@Test
	public void testDeleteApplicationUnauthorizedWithoutToken() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);
		
		mockMvc
			.perform(delete("/api/applications/" + app1.getId()))
			.andExpect(status().is(403));
	}
	
	@Test
	public void testDeleteApplicationNotFoundWithToken() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(3);
		app1 = applicationRepo.save(app1);
		
		mockMvc
			.perform(delete("/api/applications/" + app1.getId()).header("Authorization", "Bearer " + token))
			.andExpect(status().is(404));
	}
	
	@Test
	public void testDeleteApplicationAuthorized() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);
		
		mockMvc
			.perform(delete("/api/applications/" + app1.getId()).header("Authorization", "Bearer " + token))
			.andExpect(status().is(200));
	}
	
	@Test
	public void testUpdateApplicationUnauthorized() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);

		app1.setCommentary("Test commentary");
		
		mockMvc
			.perform(put("/api/applications/" + app1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(403));
	}

	@Test
	public void testUpdateApplicationNotFound() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);

		app1.setCommentary("Test commentary");
		
		mockMvc
			.perform(put("/api/applications/" + app1.getId() + 1)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(404));
	}

	@Test
	public void testUpdateApplicationNoPermission() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(200);
		app1 = applicationRepo.save(app1);

		app1.setCommentary("Test commentary");
		
		mockMvc
			.perform(put("/api/applications/" + app1.getId())
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(404));
	}

	@Test
	public void testUpdateApplication() throws Exception {
		Application app1 = new Application();
		app1.setCompanyName("testcompany");
		app1.setJobTitle("testjob");
		app1.setWorkTypeId(1);
		app1.setStatusId(2);
		app1.setUserId(0);
		app1 = applicationRepo.save(app1);

		app1.setCommentary("Test commentary");
		
		mockMvc
			.perform(put("/api/applications/" + app1.getId())
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(app1)))
			.andExpect(status().is(200));
	}

}
