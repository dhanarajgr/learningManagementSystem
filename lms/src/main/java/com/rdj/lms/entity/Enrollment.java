package com.rdj.lms.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//entity/Enrollment.java
@Entity
@Table(
 name = "enrollments",
 uniqueConstraints = {
     @UniqueConstraint(
         columnNames = {"student_id", "course_id"})
 }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne
 @JoinColumn(name = "student_id", nullable = false)
 private User student;

 @ManyToOne
 @JoinColumn(name = "course_id", nullable = false)
 private Course course;

 @Column(name = "enrolled_at")
 private LocalDateTime enrolledAt;

 // ── ADD THIS ──────────────────────────────────────
 @Column(name = "expiry_date")
 private LocalDateTime expiryDate; // auto calculated

 @Column(nullable = false)
 private Boolean completed = false;

 @PrePersist
 public void prePersist() {
     this.enrolledAt = LocalDateTime.now();

     // auto calculate expiry date
     // enrolledAt + course duration months
     this.expiryDate = LocalDateTime.now()
         .plusMonths(course.getDurationMonths());
 }
}