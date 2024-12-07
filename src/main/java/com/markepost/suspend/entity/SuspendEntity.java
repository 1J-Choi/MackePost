package com.markepost.suspend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.markepost.suspend.constant.SuspendType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "suspend")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuspendEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "boardId")
	private int boardId;
	
	// 제재를 적용한 admin의 userId
	@Column(name = "adminId")
	private int adminId;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "suspendType")
	private SuspendType suspendType;
	
	@Column(name = "untillTime")
	private LocalDateTime untillTime;
	
	@Column(name = "createdAt")
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column(name = "updatedAt")
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
