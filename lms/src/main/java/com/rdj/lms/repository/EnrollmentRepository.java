package com.rdj.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rdj.lms.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	
	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
	
	Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
	
	List<Enrollment> findByStudentId(Long StudentId);
	
	List<Enrollment> findByCourseId(Long courseId);
	
	long countByCourseId(Long CourseId);
	
	List<Enrollment> findByStudentIdAndCompleted(Long studentId,Boolean completed);
	
	
	

}
