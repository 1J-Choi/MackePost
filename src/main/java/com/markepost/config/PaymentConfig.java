package com.markepost.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
	@Value("${payment.toss.test_client_api_key}")
	private String ClientKey;
	
	@Value("${payment.toss.test_secreate_api_key}")
	private String SecretKey;
}
