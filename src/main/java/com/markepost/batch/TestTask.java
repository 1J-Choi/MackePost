package com.markepost.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TestTask {
	// 1분마다 한번씩 수행
	// @Scheduled(cron = "0 */1 * * * *")
	public void testJob() {
		log.info("#######################  Job 수행");
	}
}
