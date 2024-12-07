package com.markepost.suspend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.suspend.entity.SuspendEntity;

public interface SuspendRepository extends JpaRepository<SuspendEntity, Integer>{

}
