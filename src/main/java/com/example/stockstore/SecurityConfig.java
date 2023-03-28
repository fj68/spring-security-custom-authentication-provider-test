package com.example.stockstore;

import java.util.Arrays;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.stockstore.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final DataSource dataSource;
	private final UserRepository userRepository;

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		// add authentication filter
		final var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		final var provider = new UsernameAndPasswordAuthenticationProvider(passwordEncoder, userRepository);
		final var manager = new ProviderManager(Arrays.asList(provider));
		http.addFilter(new JWTAuthenticationFilter(manager));

		// configure access controls
		http.authorizeHttpRequests().requestMatchers("/", "/signup", "/signin").permitAll().anyRequest()
				.authenticated();
		return http.build();
	}

	@Bean
	SqlSessionFactoryBean sqlSessionFactory() {
		var sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis-config.xml"));
		return sessionFactoryBean;
	}

	@Bean
	PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
}
