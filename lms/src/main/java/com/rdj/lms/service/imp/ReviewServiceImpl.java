package com.rdj.lms.service.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rdj.lms.dto.request.CreateReviewRequest;
import com.rdj.lms.dto.response.ReviewResponse;
import com.rdj.lms.entity.Course;
import com.rdj.lms.entity.Review;
import com.rdj.lms.entity.Role;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.AlreadyExistsException;
import com.rdj.lms.exception.BadRequestException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.mapper.ReviewMapper;
import com.rdj.lms.repository.CourseRepository;
import com.rdj.lms.repository.EnrollmentRepository;
import com.rdj.lms.repository.ReviewRepository;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.repository.EnrollmentRepository;
import com.rdj.lms.service.ReviewService;

//service/impl/ReviewServiceImpl.java
@Service
public class ReviewServiceImpl implements ReviewService {

 @Autowired
 private ReviewRepository reviewRepository;

 @Autowired
 private CourseRepository courseRepository;

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private EnrollmentRepository enrollmentRepository;

 @Autowired
 private ReviewMapper reviewMapper;

 // ─── HELPER — Get Logged In User ──────────────────
 private User getLoggedInUser() {
     String email = SecurityContextHolder
                     .getContext()
                     .getAuthentication()
                     .getName();
     return userRepository.findByEmail(email)
         .orElseThrow(() -> new ResourceNotFoundException(
             "User not found"));
 }

 // ─── HELPER — Get Course ──────────────────────────
 private Course getCourse(Long courseId) {
     return courseRepository.findById(courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Course not found with id: " + courseId));
 }

 // ─── ADD REVIEW — Student enrolled only ───────────
 @Override
 public ReviewResponse addReview(Long courseId,
                         CreateReviewRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can add reviews");
     }

     // 3. get course
     Course course = getCourse(courseId);

     // 4. check enrollment
     if (!enrollmentRepository
             .existsByStudentIdAndCourseId(
                 loggedInUser.getId(), courseId)) {
         throw new BadRequestException(
             "You must enroll in this course " +
             "to add a review");
     }

     // 5. check already reviewed
     if (reviewRepository
             .existsByStudentIdAndCourseId(
                 loggedInUser.getId(), courseId)) {
         throw new AlreadyExistsException(
             "You have already reviewed this course");
     }

     // 6. create review
     Review review = reviewMapper.toEntity(request);
     review.setStudent(loggedInUser);
     review.setCourse(course);

     // 7. save and return
     return reviewMapper.toResponse(
         reviewRepository.save(review));
 }

 // ─── GET REVIEWS BY COURSE — Public ───────────────
 @Override
 public List<ReviewResponse> getReviewsByCourse(
                                 Long courseId) {

     // 1. check course exists
     getCourse(courseId);

     // 2. get all reviews — no role check
     List<Review> reviews = reviewRepository
         .findByCourseId(courseId);

     // 3. check if no reviews
     if (reviews.isEmpty()) {
         throw new ResourceNotFoundException(
             "No reviews found for this course yet");
     }

     return reviewMapper.toResponseList(reviews);
 }

 // ─── GET MY REVIEWS — Student only ────────────────
 @Override
 public List<ReviewResponse> getMyReviews() {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can view their reviews");
     }

     // 3. get own reviews
     List<Review> reviews = reviewRepository
         .findByStudentId(loggedInUser.getId());

     // 4. check if no reviews
     if (reviews.isEmpty()) {
         throw new ResourceNotFoundException(
             "You have not reviewed any course yet");
     }

     return reviewMapper.toResponseList(reviews);
 }

 // ─── UPDATE REVIEW — Student own only ─────────────
 @Override
 public ReviewResponse updateReview(Long reviewId,
                         CreateReviewRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can update reviews");
     }

     // 3. find review
     Review review = reviewRepository.findById(reviewId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Review not found with id: " + reviewId));

     // 4. check ownership
     if (!review.getStudent().getId()
                 .equals(loggedInUser.getId())) {
         throw new BadRequestException(
             "You can only update your own review");
     }

     // 5. update fields
     if (request.getRating() != null)
         review.setRating(request.getRating());
     if (request.getComment() != null)
         review.setComment(request.getComment());

     // 6. save and return
     return reviewMapper.toResponse(
         reviewRepository.save(review));
 }

 // ─── DELETE REVIEW — Student own / Admin any ──────
 @Override
 public void deleteReview(Long reviewId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. find review
     Review review = reviewRepository.findById(reviewId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Review not found with id: " + reviewId));

     // 3. instructor cannot delete
     if (loggedInUser.getRole() == Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Instructors cannot delete reviews");
     }

     // 4. student can delete own review only
     if (loggedInUser.getRole() == Role.STUDENT) {
         if (!review.getStudent().getId()
                     .equals(loggedInUser.getId())) {
             throw new BadRequestException(
                 "You can only delete your own review");
         }
     }

     // 5. admin can delete any review directly

     // 6. delete
     reviewRepository.delete(review);
 }
}
