package com.ms.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.user.request.UserRequest;
import com.ms.user.response.UserResponse;
import com.ms.user.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	/* Create user */
	@PostMapping("/save")
	public ResponseEntity<UserResponse> saveUser(@RequestBody UserRequest userRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRequest));
	}

	/* Get all users */
	@GetMapping("/all")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok().body(userService.getAllUsers());
	}

	/* Get all users with rating details */
	@GetMapping("/all-users-with-ratings")
	public ResponseEntity<List<UserResponse>> getAllUsersWithRatings() {
		return ResponseEntity.ok().body(userService.getAllUsersWithRatings());
	}

	/* Get user by id */
	@GetMapping("/getuser")
	public ResponseEntity<UserResponse> getUserById(@RequestParam String userId) {
		return ResponseEntity.ok().body(userService.getUser(userId));
	}

	/* Get user by id with rating details */
	@GetMapping("/get-user-with-ratings")
	public ResponseEntity<UserResponse> getUserByIdWithRatings(@RequestParam String userId) {
		return ResponseEntity.ok().body(userService.getUserByIdWithRatings(userId));
	}

	/* Update user details */
	@PutMapping("/update")
	public ResponseEntity<UserResponse> updateUser(@RequestParam String userId, @RequestBody UserRequest userRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(userId, userRequest));
	}

	/* Delete user with ratings */
	@DeleteMapping("/delete-user-and-ratings")
	public ResponseEntity<String> deleteUserWithRatings(@RequestParam String userId) {
		return ResponseEntity.ok().body(userService.deleteUserWithRatings(userId));
	}
	
}
