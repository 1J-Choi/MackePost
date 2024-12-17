package com.markepost.point.bo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markepost.common.RandomService;
import com.markepost.point.entity.PointEntity;
import com.markepost.point.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointBO {
	private final PointRepository pointRepository;
	private final RandomService randomService;
	
	public PointEntity getPointEntityByuserId(int userId) {
		return pointRepository.findById(userId).orElse(null);
	}
	
	public PointEntity createPoint(int userId) {
		// 랜덤 customerKey 생성하기
		String eng_lower = "abcdefghijklmnopqrstuvwxyz";
		String eng_upper = eng_lower.toUpperCase();
		String number = "0123456789";
		String data = eng_lower + eng_upper + number;
		String customerKey = randomService.createRandCode(data, 10);
		
		PointEntity point = PointEntity.builder()
				.userId(userId)
				.point(0)
				.customerKey(customerKey)
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
