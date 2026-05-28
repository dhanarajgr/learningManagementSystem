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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.rdj.lms.dto.request.CreateCourseRequest;
import com.rdj.lms.dto.request.UpdateCourseRequest;
import com.rdj.lms.dto.response.CourseResponse;
import com.rdj.lms.service.CourseService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;



//controller/CourseController.java
@RestController
@RequestMapping("/api/courses")
public class CourseController {

 @Autowired
 private CourseService courseService;

 // ─── CREATE COURSE — Instructor only ──────────────
 @PostMapping
 public ResponseEntity<CourseResponse> createCourse(
         @Valid @RequestBody CreateCourseRequest request) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(courseService.createCourse(request));
 }

 // ─── GET ALL COURSES — Public ─────────────────────
 @GetMapping
 public ResponseEntity<List<CourseResponse>> getAllCourses() {
     return ResponseEntity
             .ok(courseService.getAllCourses());
 }

 // ─── SEARCH COURSES — Public ──────────────────────
 @GetMapping("/search")
 public ResponseEntity<List<CourseResponse>> searchCourses(
         @RequestParam String title) {
     return ResponseEntity
             .ok(courseService.searchCourses(title));
 }

 // ─── GET MY COURSES — Instructor only ─────────────
 @GetMapping("/my-courses")
 public ResponseEntity<List<CourseResponse>> getMyCourses() {
     return ResponseEntity
             .ok(courseService.getMyCourses());
 }

 // ─── GET COURSE BY ID — Public ────────────────────
 @GetMapping("/{id}")
 public ResponseEntity<CourseResponse> getCourseById(
         @PathVariable Long id) {
     return ResponseEntity
             .ok(courseService.getCourseById(id));
 }

 // ─── UPDATE COURSE — Instructor own only ──────────
 @PutMapping("/{id}")
 public ResponseEntity<CourseResponse> updateCourse(
         @PathVariable Long id,
         @RequestBody UpdateCourseRequest request) {
     return ResponseEntity
             .ok(courseService.updateCourse(id, request));
 }

 // ─── DELETE COURSE ────────────────────────────────
 @DeleteMapping("/{id}")
 public ResponseEntity<String> deleteCourse(
         @PathVariable Long id) {
     courseService.deleteCourse(id);
     return ResponseEntity
             .ok("Course deleted successfully");
 }
}
