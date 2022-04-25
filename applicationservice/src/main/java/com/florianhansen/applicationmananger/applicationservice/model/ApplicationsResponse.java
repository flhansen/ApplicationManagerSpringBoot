package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.Application;

public class ApplicationsResponse {
	
	private List<Application> applications;
	
	public ApplicationsResponse(List<Application> applications) {
		setApplications(applications);
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
}
