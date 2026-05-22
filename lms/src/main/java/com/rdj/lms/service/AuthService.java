package com.rdj.lms.service;

import com.rdj.lms.dto.request.LoginRequest;
import com.rdj.lms.dto.request.RegisterRequest;
import com.rdj.lms.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    
}
