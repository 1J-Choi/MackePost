package com.markepost.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.markepost.confirm.bo.ConfirmBO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConfirmTask {
	private final ConfirmBO confirmBO;
	
	@Scheduled(cron = "0 */1 * * * *") // 매 1분마다
	public void deleteConfirmCode() {
		int deleteCount = confirmBO.deleteOldConfirm();
		log.info("########## " + deleteCount
				+ "개의 인증코드 데이터가 삭제되었습니다.");
	}
}
