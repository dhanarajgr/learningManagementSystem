package com.rdj.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdj.lms.dto.response.EnrollmentResponse;
import com.rdj.lms.service.EnrollmentService;

//controller/EnrollmentController.java
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

 @Autowired
 private EnrollmentService enrollmentService;

 // ─── ENROLL COURSE — Student only ─────────────────
 @PostMapping("/{courseId}")
 public ResponseEntity<EnrollmentResponse> enrollCourse(
         @PathVariable Long courseId) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(enrollmentService
                     .enrollCourse(courseId));
 }

 // ─── GET MY ENROLLMENTS — Student only ────────────
 @GetMapping("/my-courses")
 public ResponseEntity<List<EnrollmentResponse>>
                                 getMyEnrollments() {
     return ResponseEntity
             .ok(enrollmentService.getMyEnrollments());
 }

 // ─── GET COURSE ENROLLMENTS — Instructor only ─────
 @GetMapping("/course/{courseId}")
 public ResponseEntity<List<EnrollmentResponse>>
             getCourseEnrollments(
                     @PathVariable Long courseId) {
     return ResponseEntity
             .ok(enrollmentService
                     .getCourseEnrollments(courseId));
 }

 // ─── COMPLETE COURSE — Student only ───────────────
 @PutMapping("/{courseId}/complete")
 public ResponseEntity<EnrollmentResponse> completeCourse(
         @PathVariable Long courseId) {
     return ResponseEntity
             .ok(enrollmentService
                     .completeCourse(courseId));
 }

 // ─── UNENROLL COURSE — Student only ───────────────
 @DeleteMapping("/{courseId}")
 public ResponseEntity<String> unenrollCourse(
         @PathVariable Long courseId) {
     enrollmentService.unenrollCourse(courseId);
     return ResponseEntity
             .ok("Unenrolled successfully");
 }
}
