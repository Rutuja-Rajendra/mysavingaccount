package com.mysavingaccount.app.service;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.entity.User;
import com.mysavingaccount.app.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public User registerUser(String name, String email, String phone, String password)
	{
		User existingUser = userRepository.findByEmail(email);
		if(existingUser != null)
		{
			throw new RuntimeException("Email already registered");
		}
		
		User user = new User();
		
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPassword(passwordEncoder.encode(password));
		user.setCreatedAt(LocalDateTime.now());
		
		
		return userRepository.save(user);
		
		
	}
	
	public User loginUser(String email, String password) {
	    if (email == null || password == null) {
	        throw new RuntimeException("Email and Password are required");
	    }

	    User existingUser = userRepository.findByEmail(email);

	    if (existingUser == null) {
	        throw new RuntimeException("User not found");
	    }

	    if (!passwordEncoder.matches(password, existingUser.getPassword())) {
	        throw new RuntimeException("Wrong password");
	    }

	    return existingUser;
	}
	
	
}
