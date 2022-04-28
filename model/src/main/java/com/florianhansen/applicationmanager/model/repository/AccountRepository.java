package com.florianhansen.applicationmanager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.florianhansen.applicationmanager.model.business.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	@Query("SELECT acc FROM account acc WHERE acc.username = ?1")
	Account findAccountByUsername(String username);
	
}
