package com.markepost.confirm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.confirm.entity.ConfirmEntity;

public interface ConfirmRepository extends JpaRepository<ConfirmEntity, Integer>{

}
