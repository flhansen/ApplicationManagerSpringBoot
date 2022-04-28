package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.business.ApplicationStatus;
import com.florianhansen.applicationmanager.model.network.OkResponse;

public class ApplicationStatusesResponse extends OkResponse {
	
	private List<ApplicationStatus> applicationStatuses;
	
	public ApplicationStatusesResponse(String message, List<ApplicationStatus> applicationStatuses) {
		super(message);
		setApplicationStatuses(applicationStatuses);
	}

	public List<ApplicationStatus> getApplicationStatuses() {
		return applicationStatuses;
	}

	public void setApplicationStatuses(List<ApplicationStatus> applicationStatuses) {
		this.applicationStatuses = applicationStatuses;
	}

}
