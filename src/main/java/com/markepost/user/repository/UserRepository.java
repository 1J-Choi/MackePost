package com.markepost.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
