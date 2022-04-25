package com.florianhansen.applicationmanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.florianhansen.applicationmanager.model.WorkType;

public interface WorkTypeRepository extends JpaRepository<WorkType, Integer> {

}
