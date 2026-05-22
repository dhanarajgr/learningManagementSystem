package com.rdj.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lessons")
@NoArgsConstructor
@AllArgsConstructor 
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String Content;
	
	@Column(name="video_url")
	private String videoUrl;
	
	@Column(name="lesson_order")
	private Integer lessonOrder;
	
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

}
