package com.markepost.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.markepost.pay.bo.PayBO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayTask {
	private final PayBO payBO;
	
	@Scheduled(cron = "0 */1 * * * *")
	public void failOldPay() {
		int deleteCount = payBO.failOldPay();
		log.info("######### " + deleteCount +"개의 오래된 결재 요청 실패 처리");
	}
}
