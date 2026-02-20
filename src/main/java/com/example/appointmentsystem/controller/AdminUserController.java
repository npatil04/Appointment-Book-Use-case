package com.example.appointmentsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.appointmentsystem.dto.CreateUserRequest;
import com.example.appointmentsystem.entity.User;
import com.example.appointmentsystem.service.AdminUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

	private final AdminUserService adminUserService;

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
		User user = adminUserService.createUser(request);
		return ResponseEntity.ok(user);
	}
}
