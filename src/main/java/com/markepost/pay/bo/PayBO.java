package com.markepost.pay.bo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markepost.common.RandomService;
import com.markepost.page.generic.Page;
import com.markepost.pay.constant.PayStatus;
import com.markepost.pay.entity.PayEntity;
import com.markepost.pay.repository.PayRepository;
import com.markepost.point.bo.PointBO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayBO {
	private final PayRepository payRepository;
	private final RandomService randomService;
	private final PointBO pointBO;
	
	@Value("${payment.toss.test_secret_api_key}")
	private String secretKey;
	
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
	
	public int paySuccess(String orderId, int payId, int amount, int userId, 
			String paymentKey) {
		PayEntity pay = payRepository.findById(payId).orElse(null);
		
		// 기존 결재 요청과 맞는 정보인지 체크
		if(!pay.getOrderId().equals(orderId) || pay.getAmount() != amount
				|| pay.getPayStatus() != PayStatus.REQUEST) {
			// 해당 조건을 해당할시 아래 과정 진행하지 않고 나가버림
			return -1;
		}
		
		// scretKey의 base64 인코딩
		String encondingSecretKey = Base64.getEncoder().encodeToString((secretKey+":").getBytes());
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
			    .header("Authorization", "Basic " + encondingSecretKey)
			    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"paymentKey\":\"" + paymentKey 
			    		+ "\",\"amount\":" + amount 
			    		+ ",\"orderId\":\"" + orderId + "\"}"))
			    .build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			log.info("############## " + response.body());
			if(response.statusCode() != 200) {
				return -2;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		pay = pay.toBuilder()
				.payStatus(PayStatus.SUCCESS)
				.build();
		payRepository.save(pay);
		
		return pointBO.addAmount(userId, amount);
	}
	
	public void payFail(int payId) {
		PayEntity pay = payRepository.findById(payId).orElse(null);
		
		pay = pay.toBuilder()
				.payStatus(PayStatus.FAIL)
				.build();
		payRepository.save(pay);
	}
	
	public Page<PayEntity> getPayPageByUserId(int userId, int page) {
		Page<PayEntity> payPage = new Page<>();
		payPage.setPageSize(5);
		payPage.setNowPage(page);
		payPage.setTotalCount(payRepository.countByUserId(userId));
		List<PayEntity> payList = payRepository.findPayListByUserId(
				userId, payPage.getPageSize(), payPage.getOffset());
		payPage.setItems(payList);
		
		return payPage;
	}
}
