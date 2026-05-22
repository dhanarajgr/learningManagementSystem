package com.rdj.lms.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rdj.lms.dto.request.LoginRequest;
import com.rdj.lms.dto.request.RegisterRequest;
import com.rdj.lms.dto.response.AuthResponse;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.AlreadyExistsException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.security.JwtUtil;
import com.rdj.lms.service.AuthService;

//service/impl/AuthServiceImpl.java
@Service
public class AuthServiceImpl implements AuthService {

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private AuthenticationManager authenticationManager;

 @Autowired
 private JwtUtil jwtUtil;

 // ─── REGISTER ─────────────────────────────────────
 @Override
 public AuthResponse register(RegisterRequest request) {

     // 1. check duplicate email
     if (userRepository.existsByEmail(request.getEmail())) {
         throw new AlreadyExistsException(
             "Email already registered: " + request.getEmail());
     }

     // 2. create user
     User user = new User();
     user.setName(request.getName());
     user.setEmail(request.getEmail());
     user.setPassword(passwordEncoder.encode(
                         request.getPassword()));
     user.setRole(request.getRole());

     // 3. save user
     userRepository.save(user);

     // 4. generate token
     String token = jwtUtil.generateToken(user.getEmail());

     // 5. return response
     return new AuthResponse(
         token,
         user.getName(),
         user.getEmail(),
         user.getRole()
     );
 }

 // ─── LOGIN ────────────────────────────────────────
 @Override
 public AuthResponse login(LoginRequest request) {

     // 1. verify email and password
     authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(
             request.getEmail(),
             request.getPassword()
         )
     );

     // 2. get user from DB
     User user = userRepository.findByEmail(request.getEmail())
         .orElseThrow(() -> new ResourceNotFoundException(
             "User not found"));

     // 3. generate token
     String token = jwtUtil.generateToken(user.getEmail());

     // 4. return response
     return new AuthResponse(
         token,
         user.getName(),
         user.getEmail(),
         user.getRole()
     );
 }
}