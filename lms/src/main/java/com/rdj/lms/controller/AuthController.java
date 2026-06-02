package com.rdj.lms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rdj.lms.dto.request.LoginRequest;
import com.rdj.lms.dto.request.RegisterRequest;
import com.rdj.lms.dto.response.AuthResponse;
import com.rdj.lms.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

 @Autowired
 private AuthService authService;

 // ─── REGISTER — Student or Instructor ─────────────
 @PostMapping("/register")
 public ResponseEntity<AuthResponse> register(
         @Valid @RequestBody
         RegisterRequest request) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(authService.register(request));
 }

 // ─── REGISTER ADMIN ───────────────────────────────
 @PostMapping("/register-admin")
 public ResponseEntity<AuthResponse> registerAdmin(
         @Valid @RequestBody
         RegisterRequest request) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(authService.registerAdmin(request));
 }

 // ─── LOGIN ────────────────────────────────────────
 @PostMapping("/login")
 public ResponseEntity<AuthResponse> login(
         @Valid @RequestBody
         LoginRequest request) {
     return ResponseEntity
             .ok(authService.login(request));
 }
}