package com.example.documentapi.service;

import com.example.documentapi.dao.IUserRepository;
import com.example.documentapi.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User create(User user) {
		//Current User authenticated with Oauth2
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();

		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}


	// Obtener usuario por nombre de usuario
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow();
	}

	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	public User getUserByName(String name) {
		return userRepository.findByUsername(name).orElseThrow();
	}



	public User update(Long id, User userDetails) {
		User user = getUserById(id);
		user.setUsername(userDetails.getUsername());
		// Actualiza otros campos según sea necesario
		return userRepository.save(user);
	}

	public void delete(String name) {
		User user = getUserByName(name);
		userRepository.delete(user);
	}


	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username).orElseThrow();
		
		if(user == null) {
			logger.error("Login failed, user " + username);
			throw new UsernameNotFoundException("Login failed, the user '"+username+"' don't exist.");
		}
		
		List<GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

	@Override
	@Transactional(readOnly=true)
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow();
	}

	@Override
	public List<User> getAll() {
		return null;
	}

}
