package com.markepost.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.confirm.entity.ConfirmEntity;
import com.markepost.point.entity.PointEntity;

public interface PointRepository extends JpaRepository<PointEntity, Integer>{

}
