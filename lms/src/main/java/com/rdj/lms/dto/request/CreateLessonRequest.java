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
public class CreateLessonRequest {
	
	@NotBlank(message = "Title is equired")
	private String title;
	
	@NotBlank(message = "Content is equired")
	private String content;
	
	private String videoUrl;
	
	@NotNull(message = "password is required")
	@Min(value = 1, message = "lesson order must started from 1")
	private Integer lessonOrder;

	
}
