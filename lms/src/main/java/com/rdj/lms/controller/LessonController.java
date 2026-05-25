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

import com.rdj.lms.dto.request.CreateLessonRequest;
import com.rdj.lms.dto.request.UpdateLessonRequest;
import com.rdj.lms.dto.response.LessonResponse;
import com.rdj.lms.service.LessonService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

//controller/LessonController.java
@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {

 @Autowired
 private LessonService lessonService;

 // ─── ADD LESSON — Instructor only ─────────────────
 @PostMapping
 public ResponseEntity<LessonResponse> addLesson(
         @PathVariable Long courseId,
         @Valid @RequestBody CreateLessonRequest request) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(lessonService.addLesson(
                     courseId, request));
 }

 // ─── GET ALL LESSONS ──────────────────────────────
 @GetMapping
 public ResponseEntity<List<LessonResponse>> getLessons(
         @PathVariable Long courseId) {
     return ResponseEntity
             .ok(lessonService
                     .getLessonsByCourse(courseId));
 }

 // ─── GET LESSON BY ID ─────────────────────────────
 @GetMapping("/{lessonId}")
 public ResponseEntity<LessonResponse> getLessonById(
         @PathVariable Long courseId,
         @PathVariable Long lessonId) {
     return ResponseEntity
             .ok(lessonService
                     .getLessonById(courseId, lessonId));
 }

 // ─── UPDATE LESSON — Instructor only ──────────────
 @PutMapping("/{lessonId}")
 public ResponseEntity<LessonResponse> updateLesson(
         @PathVariable Long courseId,
         @PathVariable Long lessonId,
         @RequestBody UpdateLessonRequest request) {
     return ResponseEntity
             .ok(lessonService.updateLesson(
                     courseId, lessonId, request));
 }

 // ─── DELETE LESSON ────────────────────────────────
 @DeleteMapping("/{lessonId}")
 public ResponseEntity<String> deleteLesson(
         @PathVariable Long courseId,
         @PathVariable Long lessonId) {
     lessonService.deleteLesson(courseId, lessonId);
     return ResponseEntity
             .ok("Lesson deleted successfully");
 }
}