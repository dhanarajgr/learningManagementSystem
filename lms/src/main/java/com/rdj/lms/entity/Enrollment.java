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

@Entity
@Table(name = "enrollments", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id","course_id"})})
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
	@JoinColumn(name = "course_id", nullable=false)
	private Course course;
	
	@Column(name = "enrolled_at")
	private LocalDateTime enrolledAt;
	
	@Column(nullable = false)
	private Boolean completed= false;
	
	@PrePersist
	public void prePersist() {
		this.enrolledAt=LocalDateTime.now();
	}

}
