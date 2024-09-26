package com.example.documentapi.dao;

import com.example.documentapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Long> {

	public User findByUsername(String username);

	public User findByPassword(String username);


}
