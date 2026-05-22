package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.request.CreateCourseRequest;
import com.rdj.lms.dto.response.CourseResponse;
import com.rdj.lms.entity.Course;

@Component
public class CourseMapper {

    // Entity → Response DTO
    public CourseResponse toResponse(Course course,
                                     long totalLessons,
                                     double averageRating) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setPrice(course.getPrice());
        response.setCreatedAt(course.getCreatedAt());

        // get instructor name from nested User object
        response.setInstructorName(course.getInstructor().getName());

        // extra fields calculated outside
        response.setTotalLessons(totalLessons);
        response.setAverageRating(averageRating);

        return response;
    }

    // Request DTO → Entity
    public Course toEntity(CreateCourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        return course;
        // instructor set separately in service
    }

    // List Entity → List Response DTO
    public List<CourseResponse> toResponseList(List<Course> courses,
                                               long totalLessons,
                                               double averageRating) {
        List<CourseResponse> list = new ArrayList();
        for (Course course : courses) {
            list.add(toResponse(course, totalLessons, averageRating));
        }
        return list;
    }
}