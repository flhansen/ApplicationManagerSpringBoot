package com.florianhansen.applicationmanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.florianhansen.applicationmanager.model.business.ApplicationStatus;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Integer> {

}
