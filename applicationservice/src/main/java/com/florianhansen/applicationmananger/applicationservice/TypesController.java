package com.florianhansen.applicationmananger.applicationservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florianhansen.applicationmanager.model.ApplicationStatus;
import com.florianhansen.applicationmanager.model.WorkType;
import com.florianhansen.applicationmanager.model.repository.ApplicationStatusRepository;
import com.florianhansen.applicationmanager.model.repository.WorkTypeRepository;
import com.florianhansen.applicationmananger.applicationservice.model.ApplicationStatusesResponse;
import com.florianhansen.applicationmananger.applicationservice.model.WorkTypesResponse;

@CrossOrigin
@RestController
@RequestMapping("api/types")
public class TypesController {

	@Autowired
	private WorkTypeRepository workTypeRepo;
	
	@Autowired
	private ApplicationStatusRepository applicationStatusRepo;
	
	@GetMapping("worktypes")
	public ResponseEntity<WorkTypesResponse> getWorkTypes() {
		List<WorkType> workTypes = workTypeRepo.findAll();
		return ResponseEntity.ok(new WorkTypesResponse(workTypes));
	}

	@GetMapping("statuses")
	public ResponseEntity<ApplicationStatusesResponse> getApplicationStatuses() {
		List<ApplicationStatus> applicationStatuses = applicationStatusRepo.findAll();
		return ResponseEntity.ok(new ApplicationStatusesResponse(applicationStatuses));
	}
	
}
