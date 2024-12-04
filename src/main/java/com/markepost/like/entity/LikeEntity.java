package com.markepost.like.entity;

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
@Table(name = "`like`") // MySQL 예약어와의 혼동 방지를 위해 ``처리
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikeEntity {
	@EmbeddedId
	private LikeId likeId;
	
	@Column(name = "createdAt")
	@CreationTimestamp
	private LocalDateTime createdAt;
}
