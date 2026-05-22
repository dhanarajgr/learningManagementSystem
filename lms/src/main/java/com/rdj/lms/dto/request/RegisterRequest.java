package com.rdj.lms.dto.request;

import com.rdj.lms.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message =  "email is required")
	@Email(message = "Enter Correct email")
	private String email;
	
	@NotBlank(message = "password is required")
	@Size(min = 6, message = "password hava atleast 6 char")
	private String password;
	
	@NotNull(message = "Role is required")
	private Role role; 
	

}
