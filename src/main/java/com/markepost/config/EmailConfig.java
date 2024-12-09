package com.markepost.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	// 유저명과 앱 비밀번호 세팅
	@Value("$(spring.mail.username)")
	private String username;
	@Value("$(spring.mail.password)")
	private String password;
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com"); // 서버 주소
		javaMailSender.setPort(587); // TLS port
		javaMailSender.setUsername(username); // 이메일
		javaMailSender.setPassword(password); // 앱 비밀번호
		
		// 세부설정
		Properties javaMailProperties = new Properties();
        // 사용자 프로토콜 smtp 지정
		javaMailProperties.put("mail.transport.protocol", "smtp");
        // smtp 인증 활성화
		javaMailProperties.put("mail.smtp.auth", "true");
        // Gmail에서 사용하는 javax.net.ssl.SSLSocketFactory로 SSL 소켓 팩토리 설정
		javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // TLS 암호 활성화, Google SMTP 사용시 필수
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
        // 디버그 활성화(상세 로그 받을 수있도록)
		javaMailProperties.put("mail.debug", "true");
        // smtp.gmail.com 신뢰하도록 설정
		javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // SSL/TLS 암호화 프로토콜 버전 중 TLS v1.3을 사용
		javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.3");
		
		javaMailSender.setJavaMailProperties(javaMailProperties);
		
		return javaMailSender;
	}
}
