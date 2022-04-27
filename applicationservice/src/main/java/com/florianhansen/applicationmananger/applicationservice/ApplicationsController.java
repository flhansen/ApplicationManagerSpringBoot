package com.florianhansen.applicationmananger.applicationservice;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florianhansen.applicationmanager.authentication.service.TokenAuthenticationFacade;
import com.florianhansen.applicationmanager.authentication.util.JwtUtil;
import com.florianhansen.applicationmanager.model.Application;
import com.florianhansen.applicationmanager.model.network.OkResponse;
import com.florianhansen.applicationmanager.model.repository.ApplicationRepository;
import com.florianhansen.applicationmananger.applicationservice.model.ApplicationResponse;
import com.florianhansen.applicationmananger.applicationservice.model.ApplicationsResponse;

@CrossOrigin
@RestController
@RequestMapping("api/applications")
public class ApplicationsController {
	
	@Autowired
	private TokenAuthenticationFacade authFacade;
	
	@Autowired
	private ApplicationRepository applicationRepo;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping
	public ResponseEntity<?> getApplications() {
		String token = authFacade.getToken();
		Integer userId = jwtUtil.getUserId(token);
		
		List<Application> applications = applicationRepo.findApplicationsByUserId(userId);
		return ResponseEntity.ok(new ApplicationsResponse("Fetched applications successfully", applications));
	}
	
	@GetMapping("{applicationId}")
	public ResponseEntity<?> getApplication(@PathVariable int applicationId, Authentication authentication) {
		String token = authFacade.getToken();
		Integer userId = jwtUtil.getUserId(token);
		
		Application application = null;
		
		try {
			application = applicationRepo.getById(applicationId);

			if (application.getUserId() != userId)
				throw new EntityNotFoundException();

		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new ApplicationResponse("Fetched application successfully", application));
	}
	
	@PostMapping
	public ResponseEntity<?> createApplication(@RequestBody Application request) {
		String token = authFacade.getToken();
		Integer userId = jwtUtil.getUserId(token);

		request.setUserId(userId);
		
		List<Application> applications = applicationRepo.findAll();
		applications.add(request);
		applicationRepo.saveAll(applications);

		return ResponseEntity.ok(new OkResponse("New application has been created"));
	}
	
	@DeleteMapping("{applicationId}")
	public ResponseEntity<?> deleteApplication(@PathVariable int applicationId) {
		String token = authFacade.getToken();
		Integer userId = jwtUtil.getUserId(token);
		
		try {
			Application application = applicationRepo.getById(applicationId);
			
			if (application.getUserId() != userId)
				throw new EntityNotFoundException();
			
			applicationRepo.delete(application);
			return ResponseEntity.ok(new OkResponse("Application has been deleted"));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("{applicationId}")
	public ResponseEntity<?> updateApplication(@PathVariable int applicationId, @RequestBody Application request) {
		String token = authFacade.getToken();
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

			return ResponseEntity.ok(new OkResponse("Application has been updated"));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
