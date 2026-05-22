package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
	
	    private Long id;
	    private String title;
	    private String description;
	    private Double price;
	    private String instructorName;  // only name not full user object
	    private long totalLessons;      // count of lessons
	    private double averageRating;   // avg of all reviews
	    private LocalDateTime createdAt;
	    

}
