package com.rdj.lms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rdj.lms.dto.request.UpdateUserRequest;
import com.rdj.lms.dto.response.UserResponse;
import com.rdj.lms.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

//controller/UserController.java
@RestController
@RequestMapping("/api/users")
public class UserController {

 @Autowired
 private UserService userService;

 // ─── GET ALL USERS — Admin only ───────────────────
 @GetMapping
 public ResponseEntity<List<UserResponse>> getAllUsers() {
     return ResponseEntity
             .ok(userService.getAllUsers());
 }

 // ─── GET MY PROFILE ───────────────────────────────
 @GetMapping("/me")
 public ResponseEntity<UserResponse> getMyProfile() {
     return ResponseEntity
             .ok(userService.getMyProfile());
 }

 // ─── GET USER BY ID ───────────────────────────────
 @GetMapping("/{id}")
 public ResponseEntity<UserResponse> getUserById(
         @PathVariable Long id) {
     return ResponseEntity
             .ok(userService.getUserById(id));
 }

 // ─── UPDATE USER ──────────────────────────────────
 @PutMapping("/{id}")
 public ResponseEntity<UserResponse> updateUser(
         @PathVariable Long id,
         @RequestBody UpdateUserRequest request) {
     return ResponseEntity
             .ok(userService.updateUser(id, request));
 }

 // ─── DELETE USER — Admin only ─────────────────────
 @DeleteMapping("/{id}")
 public ResponseEntity<String> deleteUser(
         @PathVariable Long id) {
     userService.deleteUser(id);
     return ResponseEntity
             .ok("User deleted successfully");
 }
}