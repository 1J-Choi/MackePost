package com.markepost.pay.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.markepost.pay.constant.PayStatus;
import com.markepost.pay.entity.PayEntity;

public interface PayRepository extends JpaRepository<PayEntity, Integer>{
	public int countByUserId(int userId);
	@Query(value = "SELECT * FROM pay WHERE userId = :userId "
			+ "ORDER BY id DESC LIMIT :limit "
			+ "OFFSET :offset", 
			nativeQuery = true)
	public List<PayEntity> findPayListByUserId(
			@Param("userId") int userId, 
			@Param("limit") int limit, 
			@Param("offset") int offset);
	
	@Modifying
    @Query(value = "UPDATE pay SET payStatus = :status " 
	+ "WHERE createdAt < :limitTime AND payStatus = 'REQUEST'", nativeQuery = true)
    public int updateOldPayStatus(@Param("status") String status, @Param("limitTime") LocalDateTime limitTime);
}
