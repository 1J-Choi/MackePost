package com.markepost.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.pay.entity.PayEntity;

public interface PayRepository extends JpaRepository<PayEntity, Integer>{

}
