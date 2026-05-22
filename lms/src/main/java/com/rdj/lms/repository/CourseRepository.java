package com.rdj.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rdj.lms.entity.Course;
import com.rdj.lms.entity.User;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
	List<Course> findByInstructor(User instructor);
	
	List<Course> findByInstructorId(Long id);
	
	List<Course> findByTitleContainingIgnoreCase(String title);
	
	boolean existsByIdAndInstructorId(Long courseId, Long InstructorId);

}
