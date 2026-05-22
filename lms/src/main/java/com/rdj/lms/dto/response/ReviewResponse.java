package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
	
	    private Long id;
	    private Integer rating;
	    private String comment;
	    private String studentName;  // only name not full user object
	    private String courseTitle;  // only title not full course object
	    private LocalDateTime createdAt;

}
