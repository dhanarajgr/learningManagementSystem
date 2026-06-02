package com.rdj.lms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//dto/request/CreateCourseRequest.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

 @NotBlank(message = "Title is required")
 private String title;

 @NotBlank(message = "Description is required")
 private String description;

 @NotNull(message = "Price is required")
 @Min(value = 0, message = "Price cannot be negative")
 private Double price;

 // ── ADD THIS ──────────────────────────────────────
 @NotNull(message = "Duration is required")
 @Min(value = 1, message = "Duration must be at least 1 month")
 private Integer durationMonths;
}