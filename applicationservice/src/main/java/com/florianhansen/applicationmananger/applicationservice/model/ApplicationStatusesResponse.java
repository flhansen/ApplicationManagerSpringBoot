package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.ApplicationStatus;

public class ApplicationStatusesResponse {
	
	private List<ApplicationStatus> applicationStatuses;
	
	public ApplicationStatusesResponse(List<ApplicationStatus> applicationStatuses) {
		setApplicationStatuses(applicationStatuses);
	}

	public List<ApplicationStatus> getApplicationStatuses() {
		return applicationStatuses;
	}

	public void setApplicationStatuses(List<ApplicationStatus> applicationStatuses) {
		this.applicationStatuses = applicationStatuses;
	}

}
