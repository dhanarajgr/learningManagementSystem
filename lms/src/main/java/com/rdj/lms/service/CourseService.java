package com.rdj.lms.service;

import java.util.List;

import com.rdj.lms.dto.request.CreateCourseRequest;
import com.rdj.lms.dto.request.UpdateCourseRequest;
import com.rdj.lms.dto.response.CourseResponse;

//service/CourseService.java
public interface CourseService {

 // INSTRUCTOR only
 CourseResponse createCourse(CreateCourseRequest request);

 // PUBLIC — no token needed
 List<CourseResponse> getAllCourses();

 // PUBLIC — search by title
 List<CourseResponse> searchCourses(String title);

 // PUBLIC — no token needed
 CourseResponse getCourseById(Long id);

 // INSTRUCTOR — own courses only
 List<CourseResponse> getMyCourses();

 // INSTRUCTOR — own course only
 CourseResponse updateCourse(Long id,
                     UpdateCourseRequest request);

 // INSTRUCTOR own / ADMIN any
 void deleteCourse(Long id);
}
