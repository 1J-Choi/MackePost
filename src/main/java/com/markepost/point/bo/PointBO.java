package com.markepost.point.bo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markepost.point.entity.PointEntity;
import com.markepost.point.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointBO {
	private final PointRepository pointRepository;
	
	public PointEntity getPointEntityByuserId(int userId) {
		return pointRepository.findById(userId).orElse(null);
	}
	
	public PointEntity createPoint(int userId) {
		PointEntity point = PointEntity.builder()
				.userId(userId)
				.point(0)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		return pointRepository.save(point);
	}
	
	public int addAmount(int userId, int amount) {
		PointEntity point = getPointEntityByuserId(userId);
		int nowPoint = point.getPoint();
		
		point = point.toBuilder()
				.point(nowPoint + amount)
				.updatedAt(LocalDateTime.now())
				.build();
		pointRepository.save(point);
		return point.getPoint();
	}
}
