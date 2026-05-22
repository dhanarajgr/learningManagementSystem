package com.rdj.lms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class updateLessonRequest {

	
	private String title;
	private String content;
	private String videoUrl;
	private Integer lessonOrder;
}
