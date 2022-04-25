package com.florianhansen.applicationmananger.applicationservice;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.florianhansen.applicationmanager.jwt.util.JwtUtil;
import com.florianhansen.applicationmanager.model.Application;
import com.florianhansen.applicationmanager.model.repository.ApplicationRepository;
import com.florianhansen.applicationmananger.applicationservice.model.ApplicationResponse;
import com.florianhansen.applicationmananger.applicationservice.model.ApplicationsResponse;


@RestController
@RequestMapping("api/applications")
public class ApplicationsController {
	
	@Autowired
	private ApplicationRepository applicationRepo;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping
	public ResponseEntity<?> getApplications() {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		Integer userId = jwtUtil.getUserId(token);
		
		List<Application> applications = applicationRepo.findApplicationsByUserId(userId);
		return ResponseEntity.ok(new ApplicationsResponse(applications));
	}
	
	@GetMapping("{applicationId}")
	public ResponseEntity<?> getApplication(@PathVariable int applicationId) {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		Integer userId = jwtUtil.getUserId(token);
		
		Application application = null;
		
		try {
			application = applicationRepo.getById(applicationId);

			if (application.getUserId() != userId)
				throw new EntityNotFoundException();

		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new ApplicationResponse(application));
	}
	
	@PostMapping
	public ResponseEntity<?> createApplication(@RequestBody Application request) {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		Integer userId = jwtUtil.getUserId(token);

		request.setUserId(userId);
		
		List<Application> applications = applicationRepo.findAll();
		applications.add(request);
		applicationRepo.saveAll(applications);

		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("{applicationId}")
	public ResponseEntity<?> deleteApplication(@PathVariable int applicationId) {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		Integer userId = jwtUtil.getUserId(token);
		
		try {
			Application application = applicationRepo.getById(applicationId);
			
			if (application.getUserId() != userId)
				throw new EntityNotFoundException();
			
			applicationRepo.delete(application);
			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("{applicationId}")
	public ResponseEntity<?> updateApplication(@PathVariable int applicationId, @RequestBody Application request) {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
		Integer userId = jwtUtil.getUserId(token);
		
		try {
			Application application = applicationRepo.getById(applicationId);
			
			if (application.getUserId() != userId)
				throw new EntityNotFoundException();
			
			application.setAcceptedSalary(request.getAcceptedSalary());
			application.setCommentary(request.getCommentary());
			application.setCompanyName(request.getCompanyName());
			application.setJobTitle(request.getJobTitle());
			application.setStartDate(request.getStartDate());
			application.setStatusId(request.getStatusId());
			application.setSubmissionDate(request.getSubmissionDate());
			application.setWantedSalary(request.getWantedSalary());
			application.setWorkTypeId(request.getWorkTypeId());
			applicationRepo.save(application);

			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
