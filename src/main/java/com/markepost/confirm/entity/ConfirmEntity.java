package com.markepost.confirm.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "confirm")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmEntity {
	@Id
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "confirmCode")
	private String confirmCode;
	
	@Column(name = "createdAt")
	@CreationTimestamp
	private LocalDateTime createdAt;
}
