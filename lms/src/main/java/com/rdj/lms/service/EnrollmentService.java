package com.rdj.lms.service;

import java.util.List;

import com.rdj.lms.dto.response.EnrollmentResponse;

//service/EnrollmentService.java
public interface EnrollmentService {

 // STUDENT only
 EnrollmentResponse enrollCourse(Long courseId);

 // STUDENT — own enrollments only
 List<EnrollmentResponse> getMyEnrollments();

 // INSTRUCTOR — own course students only
 List<EnrollmentResponse> getCourseEnrollments(
                             Long courseId);

 // STUDENT — own enrollment only
 EnrollmentResponse completeCourse(Long courseId);

 // STUDENT — own enrollment only
 void unenrollCourse(Long courseId);
}
