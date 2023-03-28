package com.example.stockstore;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UserAuthentication implements Authentication {
	@Data
	public static class Principal implements AuthenticatedPrincipal {
		private final String username;

		@Override
		public String getName() {
			return username;
		}
	}

	private static final long serialVersionUID = 1L;

	private final Collection<? extends GrantedAuthority> authorities = List.of();
	private final Object details = null;
	private final Object credentials = null;
	private final Principal principal;
	private boolean authenticated;

	public UserAuthentication(final String username) {
		principal = new Principal(username);
		authenticated = true;
	}

	@Override
	public String getName() {
		return principal.getUsername();
	}
}