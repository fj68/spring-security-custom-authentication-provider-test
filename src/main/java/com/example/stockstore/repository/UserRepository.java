package com.example.stockstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.stockstore.entity.User;
import com.example.stockstore.mapper.UserMapper;

@Repository
public class UserRepository {
	private final UserMapper mapper;

	public UserRepository(UserMapper mapper) {
		this.mapper = mapper;
	}

	public void create(User user) {
		mapper.insert(user);
	}

	public Optional<User> get(String username) {
		return Optional.ofNullable(mapper.select(username));
	}

	public List<User> getAll() {
		return mapper.selectAll();
	}

	public void update(User user) {
		mapper.update(user);
	}
}
