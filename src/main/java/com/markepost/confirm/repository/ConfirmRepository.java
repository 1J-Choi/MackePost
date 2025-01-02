package com.markepost.confirm.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.confirm.entity.ConfirmEntity;

public interface ConfirmRepository extends JpaRepository<ConfirmEntity, Integer>{
	public int deleteByExpiredAtBefore(LocalDateTime limitTime);
}
