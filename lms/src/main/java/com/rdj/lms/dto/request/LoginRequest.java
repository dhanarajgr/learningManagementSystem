package com.rdj.lms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message =  "email is required")
	@Email(message = "Enter Correct email")
	private String email;
	
	@NotBlank(message = "password is required")
	private String password;
	
}
