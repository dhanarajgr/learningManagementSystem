package com.rdj.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rdj.lms.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	
	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
	
	Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
	
	List<Enrollment> findByStudentId(Long StudentId);
	
	List<Enrollment> findByCourseId(Long courseId);
	
	long countByCourseId(Long CourseId);
	
	List<Enrollment> findByStudentIdAndCompleted(Long studentId,Boolean completed);
	
	 @Query("SELECT COUNT(e) > 0 FROM Enrollment e " +
	           "WHERE e.student.id = :studentId " +
	           "AND e.course.instructor.id = :instructorId")
	    boolean existsByStudentIdAndInstructorId(
	        @Param("studentId") Long studentId,
	        @Param("instructorId") Long instructorId);
	 
	
	
	

}
