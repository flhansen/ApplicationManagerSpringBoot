package com.florianhansen.applicationmanager.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.florianhansen.applicationmanager.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
	
	@Query("SELECT app FROM application app WHERE user_id = ?1")
	List<Application> findApplicationsByUserId(int userId);

}
