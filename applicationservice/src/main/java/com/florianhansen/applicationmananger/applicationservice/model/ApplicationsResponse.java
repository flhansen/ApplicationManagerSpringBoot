package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.business.Application;
import com.florianhansen.applicationmanager.model.network.OkResponse;

public class ApplicationsResponse extends OkResponse {
	
	private List<Application> applications;
	
	public ApplicationsResponse(String message, List<Application> applications) {
		super(message);
		setApplications(applications);
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
}
