package com.example.stockstore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.stockstore.entity.User;

@Mapper
public interface UserMapper {
	int changePassword(String username, String password);

	int delete(String username);

	int insert(User user);

	User select(String username);

	List<User> selectAll();

	int update(User user);
}
