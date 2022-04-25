package com.florianhansen.applicationmananger.applicationservice.model;

import com.florianhansen.applicationmanager.model.Application;

public class ApplicationResponse {

	private Application application;
	
	public ApplicationResponse(Application application) {
		setApplication(application);
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
}
