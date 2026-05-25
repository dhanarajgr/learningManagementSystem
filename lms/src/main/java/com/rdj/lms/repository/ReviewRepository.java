package com.rdj.lms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rdj.lms.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

	Optional<Review> findByStudentIdAndCourseId(Long studentId, Long courseId);

	List<Review> findByCourseId(Long courseId);

	List<Review> findByStudentId(Long studentId);

	@Query("SELECT AVG(r.rating) FROM Review r " + "WHERE r.course.id = :courseId")
	Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

}
