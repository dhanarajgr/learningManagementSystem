package com.rdj.lms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
	
	@NotBlank(message = "Title is required")
	private String title;
	
	@NotBlank(message = "description is required")
	private String description;
	
	@NotNull(message = "prive is required")
	@Min(value = 0, message = "price cannot negative")
	private Double price;

}
