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

import com.rdj.lms.dto.request.CreateReviewRequest;
import com.rdj.lms.dto.response.ReviewResponse;
import com.rdj.lms.service.ReviewService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

//controller/ReviewController.java
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

 @Autowired
 private ReviewService reviewService;

 // ─── ADD REVIEW — Student enrolled only ───────────
 @PostMapping("/{courseId}")
 public ResponseEntity<ReviewResponse> addReview(
         @PathVariable Long courseId,
         @Valid @RequestBody CreateReviewRequest request) {
     return ResponseEntity
             .status(HttpStatus.CREATED)
             .body(reviewService
                     .addReview(courseId, request));
 }

 // ─── GET REVIEWS BY COURSE — Public ───────────────
 @GetMapping("/{courseId}")
 public ResponseEntity<List<ReviewResponse>>
             getReviewsByCourse(
                     @PathVariable Long courseId) {
     return ResponseEntity
             .ok(reviewService
                     .getReviewsByCourse(courseId));
 }

 // ─── GET MY REVIEWS — Student only ────────────────
 @GetMapping("/my-reviews")
 public ResponseEntity<List<ReviewResponse>>
                                 getMyReviews() {
     return ResponseEntity
             .ok(reviewService.getMyReviews());
 }

 // ─── UPDATE REVIEW — Student own only ─────────────
 @PutMapping("/{reviewId}")
 public ResponseEntity<ReviewResponse> updateReview(
         @PathVariable Long reviewId,
         @Valid @RequestBody CreateReviewRequest request) {
     return ResponseEntity
             .ok(reviewService
                     .updateReview(reviewId, request));
 }

 // ─── DELETE REVIEW ────────────────────────────────
 @DeleteMapping("/{reviewId}")
 public ResponseEntity<String> deleteReview(
         @PathVariable Long reviewId) {
     reviewService.deleteReview(reviewId);
     return ResponseEntity
             .ok("Review deleted successfully");
 }
}