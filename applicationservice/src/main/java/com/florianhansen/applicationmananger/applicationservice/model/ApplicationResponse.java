package com.florianhansen.applicationmananger.applicationservice.model;

import com.florianhansen.applicationmanager.model.business.Application;
import com.florianhansen.applicationmanager.model.network.OkResponse;

public class ApplicationResponse extends OkResponse {

	private Application application;
	
	public ApplicationResponse(String message, Application application) {
		super(message);
		setApplication(application);
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
}
