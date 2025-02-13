package com.markepost.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	public UserEntity findByLoginId(String loginId);
	public UserEntity findByEmail(String email);
	public List<UserEntity> findByNameLike(String name);
}
