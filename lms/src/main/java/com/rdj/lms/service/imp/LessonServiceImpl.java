package com.rdj.lms.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rdj.lms.dto.request.CreateLessonRequest;
import com.rdj.lms.dto.request.UpdateLessonRequest;
import com.rdj.lms.dto.response.LessonResponse;
import com.rdj.lms.entity.Course;
import com.rdj.lms.entity.Lesson;
import com.rdj.lms.entity.Role;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.AlreadyExistsException;
import com.rdj.lms.exception.BadRequestException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.mapper.LessonMapper;
import com.rdj.lms.repository.CourseRepository;
import com.rdj.lms.repository.EnrollmentRepository;
import com.rdj.lms.repository.LessonRepository;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.service.LessonService;

//service/impl/LessonServiceImpl.java
@Service
public class LessonServiceImpl implements LessonService {

 @Autowired
 private LessonRepository lessonRepository;

 @Autowired
 private CourseRepository courseRepository;

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private EnrollmentRepository enrollmentRepository;

 @Autowired
 private LessonMapper lessonMapper;

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

 // ─── HELPER — Check Course Ownership ──────────────
 private void checkCourseOwnership(Course course,
                                   User instructor) {
     if (!course.getInstructor().getId()
                 .equals(instructor.getId())) {
         throw new BadRequestException(
             "You are not the owner of this course");
     }
 }

 // ─── HELPER — Check Student Enrollment ────────────
 private void checkEnrollment(Long studentId,
                               Long courseId) {
     if (!enrollmentRepository
             .existsByStudentIdAndCourseId(
                 studentId, courseId)) {
         throw new BadRequestException(
             "You must enroll in this course " +
             "to access lessons");
     }
 }

 // ─── ADD LESSON ───────────────────────────────────
 @Override
 public LessonResponse addLesson(Long courseId,
                         CreateLessonRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can add lessons");
     }

     // 3. get course
     Course course = getCourse(courseId);

     // 4. check ownership
     checkCourseOwnership(course, loggedInUser);

     // 5. check duplicate lesson order
     boolean orderExists = lessonRepository
         .existsByLessonOrderAndCourseId(
             request.getLessonOrder(), courseId);

     if (orderExists) {
         throw new AlreadyExistsException(
             "Lesson with order " +
             request.getLessonOrder() +
             " already exists in this course");
     }

     // 6. create lesson
     Lesson lesson = lessonMapper.toEntity(request);
     lesson.setCourse(course);

     // 7. save and return
     return lessonMapper.toResponse(
         lessonRepository.save(lesson));
 }

 // ─── GET LESSONS BY COURSE ────────────────────────
 @Override
 public List<LessonResponse> getLessonsByCourse(
                                 Long courseId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. get course
     Course course = getCourse(courseId);

     // 3. role based access check
     if (loggedInUser.getRole() == Role.STUDENT) {
         // student must be enrolled
         checkEnrollment(loggedInUser.getId(), courseId);
     }

     if (loggedInUser.getRole() == Role.INSTRUCTOR) {
         // instructor must own the course
         checkCourseOwnership(course, loggedInUser);
     }

     // admin can access any course lessons directly

     // 4. get lessons ordered by lessonOrder
     List<Lesson> lessons = lessonRepository
         .findByCourseIdOrderByLessonOrderAsc(courseId);

     return lessonMapper.toResponseList(lessons);
 }

 // ─── GET LESSON BY ID ─────────────────────────────
 @Override
 public LessonResponse getLessonById(Long courseId,
                                     Long lessonId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. get course
     Course course = getCourse(courseId);

     // 3. role based access check
     if (loggedInUser.getRole() == Role.STUDENT) {
         // student must be enrolled
         checkEnrollment(loggedInUser.getId(), courseId);
     }

     if (loggedInUser.getRole() == Role.INSTRUCTOR) {
         // instructor must own the course
         checkCourseOwnership(course, loggedInUser);
     }

     // 4. find lesson
     Lesson lesson = lessonRepository
         .findByIdAndCourseId(lessonId, courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Lesson not found with id: " + lessonId));

     return lessonMapper.toResponse(lesson);
 }

 // ─── UPDATE LESSON ────────────────────────────────
 @Override
 public LessonResponse updateLesson(Long courseId,
                         Long lessonId,
                         UpdateLessonRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can update lessons");
     }

     // 3. get course
     Course course = getCourse(courseId);

     // 4. check ownership
     checkCourseOwnership(course, loggedInUser);

     // 5. find lesson
     Lesson lesson = lessonRepository
         .findByIdAndCourseId(lessonId, courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Lesson not found with id: " + lessonId));

     // 6. check duplicate lesson order if order changed
     if (request.getLessonOrder() != null) {
         boolean orderExists = lessonRepository
             .existsByLessonOrderAndCourseId(
                 request.getLessonOrder(), courseId);

         if (orderExists && !request.getLessonOrder()
                 .equals(lesson.getLessonOrder())) {
             throw new AlreadyExistsException(
                 "Lesson with order " +
                 request.getLessonOrder() +
                 " already exists in this course");
         }
     }

     // 7. update only fields that are sent
     if (request.getTitle() != null)
         lesson.setTitle(request.getTitle());
     if (request.getContent() != null)
         lesson.setContent(request.getContent());
     if (request.getVideoUrl() != null)
         lesson.setVideoUrl(request.getVideoUrl());
     if (request.getLessonOrder() != null)
         lesson.setLessonOrder(request.getLessonOrder());

     // 8. save and return
     return lessonMapper.toResponse(
         lessonRepository.save(lesson));
 }

 // ─── DELETE LESSON ────────────────────────────────
 @Override
 public void deleteLesson(Long courseId, Long lessonId) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. get course
     Course course = getCourse(courseId);

     // 3. student cannot delete
     if (loggedInUser.getRole() == Role.STUDENT) {
         throw new BadRequestException(
             "Students cannot delete lessons");
     }

     // 4. instructor can delete own course lessons only
     if (loggedInUser.getRole() == Role.INSTRUCTOR) {
         checkCourseOwnership(course, loggedInUser);
     }

     // 5. find lesson
     Lesson lesson = lessonRepository
         .findByIdAndCourseId(lessonId, courseId)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Lesson not found with id: " + lessonId));

     // 6. delete
     lessonRepository.delete(lesson);
 }
}