package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
	
	    private Long id;
	    private Long courseId; 
	    private String studentName;  // only name not full user object
	    private String courseTitle;  // only title not full course object
	    private LocalDateTime enrolledAt;
	    private Boolean completed;

}
