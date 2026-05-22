package com.rdj.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LessonResponse {
	
	    private Long id;
	    private String title;
	    private String content;
	    private String videoUrl;
	    private Integer lessonOrder;
	    private Long courseId;

}
