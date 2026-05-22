package com.rdj.lms.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
	
	@NotNull(message = "Rating is equired")
	@Min(value=1, message = "Rating is must be at least 1")
	@Max(value=5, message = "Rating is must be at most 5")
	private Integer rating;
	
	@NotBlank(message = "comment is equired")
	private String comment;

}
