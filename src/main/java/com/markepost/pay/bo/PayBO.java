package com.markepost.pay.bo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markepost.common.RandomService;
import com.markepost.pay.constant.PayStatus;
import com.markepost.pay.entity.PayEntity;
import com.markepost.pay.repository.PayRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PayBO {
	private final PayRepository payRepository;
	private final RandomService randomService;
	
	public PayEntity addPay(int amount, int userId) {
		// 랜덤 orderId 생성하기
		String eng_lower = "abcdefghijklmnopqrstuvwxyz";
		String eng_upper = eng_lower.toUpperCase();
		String number = "0123456789";
		// 랜덤을 생성할 대상 문자열 만든 뒤 전송
		String data = eng_lower + eng_upper + number;
		String orderId = randomService.createRandCode(data, 10);
		
		PayEntity pay = PayEntity.builder()
				.orderId(orderId)
				.userId(userId)
				.amount(amount)
				.payStatus(PayStatus.REQUEST)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		return payRepository.save(pay);
	}
}
