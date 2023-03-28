package com.example.stockstore;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.stockstore.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameAndPasswordAuthenticationProvider implements AuthenticationProvider {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		var auth = (UsernamePasswordAuthenticationToken) authentication;
		final var username = (String) auth.getPrincipal();
		final var password = (String) auth.getCredentials();

		var user = userRepository.get(username);

		var result = user.map(u -> {
			if (passwordEncoder.matches(password, u.getPassword())) {
				return new UserAuthentication(username);
			}
			return null;
		});

		return result.orElseThrow(() -> new BadCredentialsException("illegal username or password"));
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
