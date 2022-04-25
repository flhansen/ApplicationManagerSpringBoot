package com.florianhansen.applicationmanager.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JwtUser extends User {

	private static final long serialVersionUID = 3806954819923208507L;
	
	private Integer id;

	public JwtUser(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		setId(id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
