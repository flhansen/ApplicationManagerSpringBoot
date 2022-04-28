package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.business.WorkType;
import com.florianhansen.applicationmanager.model.network.OkResponse;

public class WorkTypesResponse extends OkResponse {
	
	private List<WorkType> workTypes;
	
	public WorkTypesResponse(String message, List<WorkType> workTypes) {
		super(message);
		setWorkTypes(workTypes);
	}

	public List<WorkType> getWorkTypes() {
		return workTypes;
	}

	public void setWorkTypes(List<WorkType> workTypes) {
		this.workTypes = workTypes;
	}

}
