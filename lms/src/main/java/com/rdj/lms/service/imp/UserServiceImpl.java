package com.rdj.lms.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rdj.lms.dto.request.UpdateUserRequest;
import com.rdj.lms.dto.response.UserResponse;
import com.rdj.lms.entity.Role;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.BadRequestException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.mapper.UserMapper;
import com.rdj.lms.repository.EnrollmentRepository;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	 @Autowired
	 private UserRepository userRepository;

	 @Autowired
	 private UserMapper userMapper;
	 
	 @Autowired
	 private EnrollmentRepository enrollmentRepository;
	 
	 // ─── HELPER — Get Logged In User ──────────────────
	    private User getLoggedInUser() {
	        String email = SecurityContextHolder
	                        .getContext()
	                        .getAuthentication()
	                        .getName();
	        return userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                "User not found"));
	    }
	 
	 
	
	// ─── GET ALL USERS — Admin only ───────────────────
	 @Override
	 public List<UserResponse> getAllUsers() {
	     User loggedInUser = getLoggedInUser();
	     if (loggedInUser.getRole() != Role.ADMIN) {
	         throw new BadRequestException(
	             "Only admin can view all users");
	     }
	     return userMapper.toResponseList(userRepository.findAll());
	 }

	 // ─── GET USER BY ID ───────────────────────────────
	 @Override
	 public UserResponse getUserById(Long id) {

	     // 1. get logged in user
	     User loggedInUser = getLoggedInUser();

	     // ─── ADMIN ────────────────────────────────────────
	     // admin can view anyone
	     if (loggedInUser.getRole() == Role.ADMIN) {
	         User user = userRepository.findById(id)
	             .orElseThrow(() -> new ResourceNotFoundException(
	                 "User not found with id: " + id));
	         return userMapper.toResponse(user);
	     }

	     // ─── STUDENT ──────────────────────────────────────
	     if (loggedInUser.getRole() == Role.STUDENT) {

	         // own profile — always allowed
	         if (loggedInUser.getId().equals(id)) {
	             return userMapper.toResponse(loggedInUser);
	         }

	         // viewing someone else — check if instructor
	         User targetUser = userRepository.findById(id)
	             .orElseThrow(() -> new ResourceNotFoundException(
	                 "User not found with id: " + id));

	         // target must be instructor
	         if (targetUser.getRole() != Role.INSTRUCTOR) {
	             throw new BadRequestException(
	                 "Students can only view instructor profiles");
	         }

	         // student must be enrolled in at least one
	         // of this instructor's courses
	         boolean isEnrolled = enrollmentRepository
	             .existsByStudentIdAndInstructorId(
	                 loggedInUser.getId(), id);

	         if (!isEnrolled) {
	             throw new BadRequestException(
	                 "You must enroll in this instructor's course " +
	                 "to view their profile");
	         }

	         return userMapper.toResponse(targetUser);
	     }

	     // ─── INSTRUCTOR ───────────────────────────────────
	     if (loggedInUser.getRole() == Role.INSTRUCTOR) {

	         // own profile — always allowed
	         if (loggedInUser.getId().equals(id)) {
	             return userMapper.toResponse(loggedInUser);
	         }

	         // viewing someone else — check if student
	         User targetUser = userRepository.findById(id)
	             .orElseThrow(() -> new ResourceNotFoundException(
	                 "User not found with id: " + id));

	         // target must be student
	         if (targetUser.getRole() != Role.STUDENT) {
	             throw new BadRequestException(
	                 "Instructors can only view their enrolled " +
	                 "student profiles");
	         }

	         // student must be enrolled in instructor's course
	         boolean isEnrolled = enrollmentRepository
	             .existsByStudentIdAndInstructorId(
	                 id, loggedInUser.getId());

	         if (!isEnrolled) {
	             throw new BadRequestException(
	                 "This student is not enrolled in " +
	                 "your course");
	         }

	         return userMapper.toResponse(targetUser);
	     }

	     throw new BadRequestException("Access denied");
	 }
	 // ─── GET MY PROFILE ───────────────────────────────
	 @Override
	 public UserResponse getMyProfile() {
	     return userMapper.toResponse(getLoggedInUser());
	 }

	 // ─── UPDATE USER ──────────────────────────────────
	 @Override
	 public UserResponse updateUser(Long id,
	                         UpdateUserRequest request) {
	     User loggedInUser = getLoggedInUser();

	     if (!loggedInUser.getId().equals(id)) {
	         throw new BadRequestException(
	             "You can only update your own profile");
	     }

	     User user = userRepository.findById(id)
	         .orElseThrow(() -> new ResourceNotFoundException(
	             "User not found with id: " + id));

	     if (request.getName() != null)
	         user.setName(request.getName());
	     if (request.getEmail() != null)
	         user.setEmail(request.getEmail());

	     return userMapper.toResponse(userRepository.save(user));
	 }

	 // ─── DELETE USER — Admin only ─────────────────────
	 @Override
	 public void deleteUser(Long id) {
	     User loggedInUser = getLoggedInUser();

	     if (loggedInUser.getRole() != Role.ADMIN) {
	         throw new BadRequestException(
	             "Only admin can delete users");
	     }

	     User user = userRepository.findById(id)
	         .orElseThrow(() -> new ResourceNotFoundException(
	             "User not found with id: " + id));

	     userRepository.delete(user);
	 }
	 
	 
	

}
