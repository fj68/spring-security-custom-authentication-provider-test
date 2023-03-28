package com.example.stockstore.entity;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class User {
	private String username;
	private String password;
	private String role;
	private Date registerAt;
	private Date lastLoginAt;
	private Date updateAt;

	public User(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
		registerAt = new Date();
		lastLoginAt = new Date();
		updateAt = new Date();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList(role);
	}
}
