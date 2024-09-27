package com.example.documentapi.dao;

import com.example.documentapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
