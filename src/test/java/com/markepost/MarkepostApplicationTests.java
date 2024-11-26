package com.markepost;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@Slf4j
class MarkepostApplicationTests {

	@Test
	void contextLoads() {
		// unit test
		// given
		int a = 5;
		int b = 10;
		// when
		int c = a + b;
		// then
		assertEquals(15, c);
		log.info("첫번째 메소드");
	}
	
	@Test
	void 테스트() {
		log.info("두번째 메소드");
		// given - 준비
		
		// when - 실행
		
		// then - 검증
		
	}
}
