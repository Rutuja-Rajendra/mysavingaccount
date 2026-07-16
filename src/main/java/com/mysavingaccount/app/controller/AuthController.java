package com.mysavingaccount.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysavingaccount.app.dto.LoginRequest;
import com.mysavingaccount.app.dto.RegisterRequest;
import com.mysavingaccount.app.entity.User;
import com.mysavingaccount.app.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public User registerUser(@Valid @RequestBody RegisterRequest registerRequest)
	{
		return authService.registerUser(
				registerRequest.getName(), 
				registerRequest.getEmail(),
				registerRequest.getPhone(),
				registerRequest.getPassword());
	}
	
	
	@PostMapping("/login")
	public User loginUser(@Valid @RequestBody LoginRequest loginRequest)
	{
		return authService.loginUser(
				loginRequest.getEmail(),
				loginRequest.getPassword());
	}

}
