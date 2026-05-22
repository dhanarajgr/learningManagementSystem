package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.request.CreateReviewRequest;
import com.rdj.lms.dto.response.ReviewResponse;
import com.rdj.lms.entity.Review;

@Component
public class ReviewMapper {

    // Entity → Response DTO
    public ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());

        // get student name from nested User object
        response.setStudentName(review.getStudent().getName());

        // get course title from nested Course object
        response.setCourseTitle(review.getCourse().getTitle());

        return response;
    }

    // Request DTO → Entity
    public Review toEntity(CreateReviewRequest request) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return review;
        // student and course set separately in service
    }

    // List Entity → List Response DTO
    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        List<ReviewResponse> list = new ArrayList<>();
        for (Review review : reviews) {
            list.add(toResponse(review));
        }
        return list;
    }
}