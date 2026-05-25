package com.rdj.lms.service;

import java.util.List;

import com.rdj.lms.dto.request.CreateLessonRequest;
import com.rdj.lms.dto.request.UpdateLessonRequest;
import com.rdj.lms.dto.response.LessonResponse;

//service/LessonService.java
public interface LessonService {

 // INSTRUCTOR — own course only
 LessonResponse addLesson(Long courseId,
                     CreateLessonRequest request);

 // STUDENT enrolled / INSTRUCTOR own / ADMIN any
 List<LessonResponse> getLessonsByCourse(Long courseId);

 // STUDENT enrolled / INSTRUCTOR own / ADMIN any
 LessonResponse getLessonById(Long courseId, Long lessonId);

 // INSTRUCTOR — own course only
 LessonResponse updateLesson(Long courseId, Long lessonId,
                     UpdateLessonRequest request);

 // INSTRUCTOR own / ADMIN any
 void deleteLesson(Long courseId, Long lessonId);
}
