package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import com.rdj.lms.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	
	private Long id;
	private String name;
	private String email;
	private Role role;
	private LocalDateTime createdAt;
}
