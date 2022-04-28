package com.florianhansen.applicationmanager.authentication.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.florianhansen.applicationmanager.authentication.JwtUser;
import com.florianhansen.applicationmanager.model.business.Account;
import com.florianhansen.applicationmanager.model.repository.AccountRepository;

@Service
@ComponentScan("com.florianhansen.applicationmanager.model")
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AccountRepository accountRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepo.findAccountByUsername(username);
		
		if (account == null)
			return null;
		
		return new JwtUser(account.getId(), account.getUsername(), account.getPassword(), new ArrayList<>());
	}

}
