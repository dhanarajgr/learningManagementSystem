package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.response.EnrollmentResponse;
import com.rdj.lms.entity.Enrollment;

@Component
public class EnrollmentMapper {

    // Entity → Response DTO
	// EnrollmentMapper.java
	public EnrollmentResponse toResponse(
	                            Enrollment enrollment) {
	    EnrollmentResponse response = new EnrollmentResponse();
	    response.setId(enrollment.getId());
	    response.setCourseId(
	        enrollment.getCourse().getId()); // ← add this ✅
	    response.setStudentName(
	        enrollment.getStudent().getName());
	    response.setCourseTitle(
	        enrollment.getCourse().getTitle());
	    response.setEnrolledAt(enrollment.getEnrolledAt());
	    response.setCompleted(enrollment.getCompleted());
	    return response;
	}

    // List Entity → List Response DTO
    public List<EnrollmentResponse> toResponseList(
                                    List<Enrollment> enrollments) {
        List<EnrollmentResponse> list = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            list.add(toResponse(enrollment));
        }
        return list;
    }
}