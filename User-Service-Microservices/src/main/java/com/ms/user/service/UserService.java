package com.ms.user.service;

import java.util.List;

import com.ms.user.request.UserRequest;
import com.ms.user.response.UserResponse;

public interface UserService {

	UserResponse saveUser(UserRequest userRequest);
	List<UserResponse> getAllUsers();
	List<UserResponse> getAllUsersWithRatings();
	UserResponse getUser(String userId);
	UserResponse getUserByIdWithRatings(String userId);
	UserResponse updateUser(String userId, UserRequest userRequest);
	String deleteUserWithRatings(String userId);
}
