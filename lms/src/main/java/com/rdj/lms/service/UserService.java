package com.rdj.lms.service;

import java.util.List;

import com.rdj.lms.dto.request.UpdateUserRequest;
import com.rdj.lms.dto.response.UserResponse;

//service/UserService.java
public interface UserService {

 // ADMIN only
 List<UserResponse> getAllUsers();

 // role based access
 // student → own + enrolled instructor only
 // instructor → own + enrolled students only
 // admin → anyone
 UserResponse getUserById(Long id);

 // own profile only
 UserResponse getMyProfile();

 // own profile only
 UserResponse updateUser(Long id, UpdateUserRequest request);

 // ADMIN only
 void deleteUser(Long id);
}

