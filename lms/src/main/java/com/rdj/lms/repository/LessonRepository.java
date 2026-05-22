package com.rdj.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rdj.lms.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
	
	List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);
	
	Optional<Lesson> findByIdAndCourseId(Long lessonId, Long CourseId);
	
	long countByCourseId(Long courseId);
	
	boolean existsByIdAndCourseId(Long lessonId, Long courseId);
	

}
