package com.florianhansen.applicationmananger.applicationservice.model;

import java.util.List;

import com.florianhansen.applicationmanager.model.WorkType;

public class WorkTypesResponse {
	
	private List<WorkType> workTypes;
	
	public WorkTypesResponse(List<WorkType> workTypes) {
		setWorkTypes(workTypes);
	}

	public List<WorkType> getWorkTypes() {
		return workTypes;
	}

	public void setWorkTypes(List<WorkType> workTypes) {
		this.workTypes = workTypes;
	}

}
