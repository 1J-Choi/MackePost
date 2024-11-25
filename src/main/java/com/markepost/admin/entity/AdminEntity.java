package com.markepost.admin.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "admin")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminEntity {
	
	@EmbeddedId
	private AdminId adminId;
	
	@Column(name = "adminType")
	private boolean adminType;
	
	@Column(name = "createdAt")
	@CreationTimestamp
	private LocalDateTime createdAt;
}
