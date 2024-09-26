package com.example.documentapi.service;

import com.example.documentapi.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserService {

	public User findByUsername(String username);

	List<User> getAll();

	User getUserById(Long id);

	User update(Long id, User userDetails);

	void delete(String name);

	User create(User user);
}
