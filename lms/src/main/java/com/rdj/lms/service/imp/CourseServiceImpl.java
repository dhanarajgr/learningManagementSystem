package com.rdj.lms.service.imp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rdj.lms.dto.request.CreateCourseRequest;
import com.rdj.lms.dto.request.UpdateCourseRequest;
import com.rdj.lms.dto.response.CourseResponse;
import com.rdj.lms.entity.Course;
import com.rdj.lms.entity.Role;
import com.rdj.lms.entity.User;
import com.rdj.lms.exception.BadRequestException;
import com.rdj.lms.exception.ResourceNotFoundException;
import com.rdj.lms.mapper.CourseMapper;
import com.rdj.lms.repository.CourseRepository;
import com.rdj.lms.repository.LessonRepository;
import com.rdj.lms.repository.ReviewRepository;
import com.rdj.lms.repository.UserRepository;
import com.rdj.lms.service.CourseService;

//service/impl/CourseServiceImpl.java
@Service
public class CourseServiceImpl implements CourseService {

 @Autowired
 private CourseRepository courseRepository;

 @Autowired
 private UserRepository userRepository;

 @Autowired
 private LessonRepository lessonRepository;

 @Autowired
 private ReviewRepository reviewRepository;

 @Autowired
 private CourseMapper courseMapper;

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

 // ─── HELPER — Get Total Lessons ───────────────────
 private long getTotalLessons(Long courseId) {
     return lessonRepository.countByCourseId(courseId);
 }

 // ─── HELPER — Get Average Rating ──────────────────
 private double getAverageRating(Long courseId) {
     Double avg = reviewRepository
                     .findAverageRatingByCourseId(courseId);
     return avg != null ? avg : 0.0;
 }

 // ─── HELPER — Build CourseResponse ────────────────
 private CourseResponse buildResponse(Course course) {
     return courseMapper.toResponse(
         course,
         getTotalLessons(course.getId()),
         getAverageRating(course.getId())
     );
 }

 // ─── HELPER — Build Response List ─────────────────
 private List<CourseResponse> buildResponseList(
                                 List<Course> courses) {
     List<CourseResponse> list = new ArrayList<>();
     for (Course course : courses) {
         list.add(buildResponse(course));
     }
     return list;
 }

 // ─── CREATE COURSE ────────────────────────────────
 @Override
 public CourseResponse createCourse(
                         CreateCourseRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can create courses");
     }

     // 3. create course entity
     Course course = courseMapper.toEntity(request);

     // 4. set instructor from token
     course.setInstructor(loggedInUser);

     // 5. save and return
     return buildResponse(courseRepository.save(course));
 }

 // ─── GET ALL COURSES — Public ─────────────────────
 @Override
 public List<CourseResponse> getAllCourses() {

     // no role check — public
     List<Course> courses = courseRepository.findAll();
     return buildResponseList(courses);
 }

 // ─── SEARCH COURSES — Public ──────────────────────
 @Override
 public List<CourseResponse> searchCourses(String title) {

     // no role check — public
     List<Course> courses = courseRepository
         .findByTitleContainingIgnoreCase(title);

     if (courses.isEmpty()) {
         throw new ResourceNotFoundException(
             "No courses found with title: " + title);
     }

     return buildResponseList(courses);
 }

 // ─── GET COURSE BY ID — Public ────────────────────
 @Override
 public CourseResponse getCourseById(Long id) {

     // no role check — public
     Course course = courseRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Course not found with id: " + id));

     return buildResponse(course);
 }

 // ─── GET MY COURSES — Instructor ──────────────────
 @Override
 public List<CourseResponse> getMyCourses() {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can view their courses");
     }

     // 3. get own courses
     List<Course> courses = courseRepository
         .findByInstructorId(loggedInUser.getId());

     return buildResponseList(courses);
 }

 // ─── UPDATE COURSE — Instructor own only ──────────
 @Override
 public CourseResponse updateCourse(Long id,
                         UpdateCourseRequest request) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. check instructor role
     if (loggedInUser.getRole() != Role.INSTRUCTOR) {
         throw new BadRequestException(
             "Only instructors can update courses");
     }

     // 3. find course
     Course course = courseRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Course not found with id: " + id));

     // 4. check ownership
     if (!course.getInstructor().getId()
                 .equals(loggedInUser.getId())) {
         throw new BadRequestException(
             "You are not the owner of this course");
     }

     // 5. update only fields that are sent
     if (request.getTitle() != null)
         course.setTitle(request.getTitle());
     if (request.getDescription() != null)
         course.setDescription(request.getDescription());
     if (request.getPrice() != null)
         course.setPrice(request.getPrice());

     // 6. save and return
     return buildResponse(courseRepository.save(course));
 }

 // ─── DELETE COURSE — Instructor own / Admin any ───
 @Override
 public void deleteCourse(Long id) {

     // 1. get logged in user
     User loggedInUser = getLoggedInUser();

     // 2. find course
     Course course = courseRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException(
             "Course not found with id: " + id));

     // 3. instructor can delete own course only
     if (loggedInUser.getRole() == Role.INSTRUCTOR) {
         if (!course.getInstructor().getId()
                     .equals(loggedInUser.getId())) {
             throw new BadRequestException(
                 "You are not the owner of this course");
         }
     }

     // 4. student cannot delete
     if (loggedInUser.getRole() == Role.STUDENT) {
         throw new BadRequestException(
             "Students cannot delete courses");
     }

     // 5. admin and course owner can delete
     // cascade deletes lessons, enrollments, reviews
     courseRepository.delete(course);
 }
}
