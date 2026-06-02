package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.request.CreateCourseRequest;
import com.rdj.lms.dto.response.CourseResponse;
import com.rdj.lms.entity.Course;

//mapper/CourseMapper.java
@Component
public class CourseMapper {

 public CourseResponse toResponse(Course course,
                                  long totalLessons,
                                  double averageRating) {
     CourseResponse response = new CourseResponse();
     response.setId(course.getId());
     response.setTitle(course.getTitle());
     response.setDescription(course.getDescription());
     response.setPrice(course.getPrice());
     response.setDurationMonths(
         course.getDurationMonths()); // ← add this ✅
     response.setCreatedAt(course.getCreatedAt());
     response.setInstructorName(
         course.getInstructor().getName());
     response.setTotalLessons(totalLessons);
     response.setAverageRating(averageRating);
     return response;
 }

 public Course toEntity(CreateCourseRequest request) {
     Course course = new Course();
     course.setTitle(request.getTitle());
     course.setDescription(request.getDescription());
     course.setPrice(request.getPrice());
     course.setDurationMonths(
         request.getDurationMonths()); // ← add this ✅
     return course;
 }

 public List<CourseResponse> toResponseList(
                                 List<Course> courses,
                                 long totalLessons,
                                 double averageRating) {
     List<CourseResponse> list = new ArrayList<>();
     for (Course course : courses) {
         list.add(toResponse(course,
             totalLessons, averageRating));
     }
     return list;
 }
}