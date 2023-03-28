package com.example.stockstore;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Data
	public class LoginForm {
		private String username;
		private String password;
	}

	private static final String SECRET = "SecretKeyToGenJWTs";
	private static final long EXPIRATION_TIME = 864_000_000; // 10 days
	private static final String TOKEN_PREFIX = "Bearer ";

	private static final String HEADER_STRING = "Authorization";

	public JWTAuthenticationFilter(final AuthenticationManager authenticationManager) {
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			final var form = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);

			final var credentials = new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword());

			return getAuthenticationManager().authenticate(credentials);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res,
			final FilterChain chain, final Authentication auth) throws IOException, ServletException {

		final var token = JWT.create().withSubject(auth.getName())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(HMAC512(SECRET.getBytes()));
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
}
