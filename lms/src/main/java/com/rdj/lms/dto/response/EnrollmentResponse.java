package com.rdj.lms.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//dto/response/EnrollmentResponse.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

 private Long id;
 private Long courseId;
 private String studentName;
 private String courseTitle;
 private LocalDateTime enrolledAt;
 private LocalDateTime expiryDate; // ← add this ✅
 private Boolean completed;
}
