package com.rdj.lms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(nullable = false)
	private Double price;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "instructor_id", nullable = false)
	private User instructor;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL )
	@OrderBy("lessonOrder ASC")
	@JsonIgnore
	private List<Lesson> lessons = new ArrayList<Lesson>();
	
	@OneToMany(mappedBy = "course", cascade =CascadeType.ALL )
	@JsonIgnore
	private List<Enrollment> enrollments = new ArrayList<Enrollment>() ;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> reviews = new ArrayList<Review>() ;
	
	
	@PrePersist
	public void prePersist() {
		this.createdAt=LocalDateTime.now();
	}

}
