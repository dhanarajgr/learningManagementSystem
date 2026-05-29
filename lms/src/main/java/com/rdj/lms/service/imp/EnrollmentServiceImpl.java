package com.rdj.lms.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rdj.lms.dto.response.EnrollmentResponse;
import com.rdj.lms.entity.Course;
import com.rdj.lms.entity.Enrollment;
import com.rdj.lms.entity.Review;
import com.rdj.lms.entity.Role;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.AlreadyExistsException;
import com.rdj.lms.exception.BadRequestException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.mapper.EnrollmentMapper;
import com.rdj.lms.repository.CourseRepository;
import com.rdj.lms.repository.EnrollmentRepository;
import com.rdj.lms.repository.ReviewRepository;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.service.EnrollmentService;

//service/impl/EnrollmentServiceImpl.java
@Service
public class EnrollmentServiceImpl
             implements EnrollmentService {

 @Autowired
 private EnrollmentRepository enrollmentRepository;

 @Autowired
 private CourseRepository courseRepository;

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private EnrollmentMapper enrollmentMapper;
 
 @Autowired
 private ReviewRepository reviewRepository;

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

 // ─── ENROLL COURSE — Student only ─────────────────
 @Override
 public EnrollmentResponse enrollCourse(Long courseId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can enroll in courses");
     }

     // 3. get course
     Course course = getCourse(courseId);

     // 4. check already enrolled
     if (enrollmentRepository
             .existsByStudentIdAndCourseId(
                 loggedInUser.getId(), courseId)) {
         throw new AlreadyExistsException(
             "You are already enrolled in this course");
     }

     // 5. check instructor enrolling own course
     if (course.getInstructor().getId()
                 .equals(loggedInUser.getId())) {
         throw new BadRequestException(
             "Instructors cannot enroll " +
             "in their own course");
     }

     // 6. create enrollment
     Enrollment enrollment = new Enrollment();
     enrollment.setStudent(loggedInUser);
     enrollment.setCourse(course);
     enrollment.setCompleted(false);

     // 7. save and return
     return enrollmentMapper.toResponse(
         enrollmentRepository.save(enrollment));
 }

 // ─── GET MY ENROLLMENTS — Student only ────────────
 @Override
 public List<EnrollmentResponse> getMyEnrollments() {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can view their enrollments");
     }

     // 3. get own enrollments
     List<Enrollment> enrollments = enrollmentRepository
         .findByStudentId(loggedInUser.getId());

     // 4. check if no enrollments
     if (enrollments.isEmpty()) {
         throw new ResourceNotFoundException(
             "You have not enrolled in any course yet");
     }

     return enrollmentMapper.toResponseList(enrollments);
 }

 // ─── GET COURSE ENROLLMENTS — Instructor only ─────
 @Override
 public List<EnrollmentResponse> getCourseEnrollments(
                                     Long courseId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can view " +
             "course enrollments");
     }

     // 3. get course
     Course course = getCourse(courseId);

     // 4. check ownership
     if (!course.getInstructor().getId()
                 .equals(loggedInUser.getId())) {
         throw new BadRequestException(
             "You are not the owner of this course");
     }

     // 5. get all enrollments for this course
     List<Enrollment> enrollments = enrollmentRepository
         .findByCourseId(courseId);

     // 6. check if no enrollments
     if (enrollments.isEmpty()) {
         throw new ResourceNotFoundException(
             "No students enrolled in this course yet");
     }

     return enrollmentMapper.toResponseList(enrollments);
 }

 // ─── COMPLETE COURSE — Student only ───────────────
 @Override
 public EnrollmentResponse completeCourse(Long courseId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can complete courses");
     }

     // 3. find enrollment
     Enrollment enrollment = enrollmentRepository
         .findByStudentIdAndCourseId(
             loggedInUser.getId(), courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "You are not enrolled in this course"));

     // 4. check already completed
     if (enrollment.getCompleted()) {
         throw new AlreadyExistsException(
             "You have already completed this course");
     }

     // 5. mark as completed
     enrollment.setCompleted(true);

     // 6. save and return
     return enrollmentMapper.toResponse(
         enrollmentRepository.save(enrollment));
 }

 // ─── UNENROLL COURSE — Student only ───────────────
 @Override
 public void unenrollCourse(Long courseId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check student role
     if (loggedInUser.getRole() != Role.STUDENT) {
         throw new BadRequestException(
             "Only students can unenroll from courses");
     }

     // 3. find enrollment
     Enrollment enrollment = enrollmentRepository
         .findByStudentIdAndCourseId(
             loggedInUser.getId(), courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "You are not enrolled in this course"));

     // 4. delete review if exists
     Optional<Review> review = reviewRepository
         .findByStudentIdAndCourseId(
             loggedInUser.getId(), courseId);

     if (review.isPresent()) {
         reviewRepository.delete(review.get());
     }

     // 5. delete enrollment
     enrollmentRepository.delete(enrollment);
 }
 
 
//─── ADMIN UNENROLL STUDENT ───────────────────────
@Override
public void adminUnenrollStudent(Long studentId,
                                Long courseId) {

  // 1. get logged in user
  User loggedInUser = getLoggedInUser();

  // 2. check admin role
  if (loggedInUser.getRole() != Role.ADMIN) {
      throw new BadRequestException(
          "Only admin can unenroll students");
  }

  // 3. check student exists
  User student = userRepository.findById(studentId)
      .orElseThrow(() -> new ResourceNotFoundException(
          "Student not found with id: " + studentId));

  // 4. check student role
  if (student.getRole() != Role.STUDENT) {
      throw new BadRequestException(
          "User is not a student");
  }

  // 5. find enrollment
  Enrollment enrollment = enrollmentRepository
      .findByStudentIdAndCourseId(studentId, courseId)
      .orElseThrow(() -> new ResourceNotFoundException(
          "Student is not enrolled in this course"));

  // 6. delete review if exists
  Optional<Review> review = reviewRepository
      .findByStudentIdAndCourseId(studentId, courseId);

  if (review.isPresent()) {
      reviewRepository.delete(review.get());
  }

  // 7. delete enrollment
  enrollmentRepository.delete(enrollment);
}
}
