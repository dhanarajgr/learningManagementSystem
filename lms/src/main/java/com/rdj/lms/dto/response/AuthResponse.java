package com.rdj.lms.dto.response;

import com.rdj.lms.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	private String token;
	private String name;
	private String email;
	private Role role;

}
