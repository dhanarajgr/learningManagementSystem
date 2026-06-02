package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//dto/response/CourseResponse.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

 private Long id;
 private String title;
 private String description;
 private Double price;
 private Integer durationMonths; // ← add this ✅
 private String instructorName;
 private long totalLessons;
 private double averageRating;
 private LocalDateTime createdAt;
}