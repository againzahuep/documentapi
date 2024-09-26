package com.example.documentapi.service;

import com.example.documentapi.dao.IUserDao;
import com.example.documentapi.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService{

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserDao userDao;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User create(User user) {

		// Crear un nuevo usuario
			// Hash de la contraseña antes de guardar
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userDao.save(user);
		}


	// Obtener usuario por nombre de usuario
	public User getUserByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public List<User> getAllUsers() {
		return (List<User>) userDao.findAll();
	}

	public User getUserById(Long id) {
		return userDao.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}

	public User getUserByName(String name) {
		return userDao.findByUsername(name);
	}



	public User update(Long id, User userDetails) {
		User user = getUserById(id);
		user.setName(userDetails.getName());
		// Actualiza otros campos según sea necesario
		return userDao.save(user);
	}

	public void delete(String name) {
		User user = getUserByName(name);
		userDao.delete(user);
	}


	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userDao.findByUsername(username);
		
		if(user == null) {
			logger.error("Login failed, user " + username);
			throw new UsernameNotFoundException("Login failed, the user '"+username+"' don't exist.");
		}
		
		List<GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
	}

	@Override
	@Transactional(readOnly=true)
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public List<User> getAll() {
		return null;
	}

}
