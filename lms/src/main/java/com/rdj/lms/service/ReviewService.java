package com.rdj.lms.service;

import java.util.List;

import com.rdj.lms.dto.request.CreateReviewRequest;
import com.rdj.lms.dto.response.ReviewResponse;

//service/ReviewService.java
public interface ReviewService {

 // STUDENT only — must be enrolled
 ReviewResponse addReview(Long courseId,
                     CreateReviewRequest request);

 // PUBLIC — no token needed
 List<ReviewResponse> getReviewsByCourse(Long courseId);

 // STUDENT — own reviews only
 List<ReviewResponse> getMyReviews();

 // STUDENT — own review only
 ReviewResponse updateReview(Long reviewId,
                     CreateReviewRequest request);

 // STUDENT own / ADMIN any
 void deleteReview(Long reviewId);
}
