package com.rdj.lms.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Course> courses;
	
	@OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Enrollment> enrollments;
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> review;
	
	
	@PrePersist
	public void prePersist() {
		this.createdAt=LocalDateTime.now();
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
