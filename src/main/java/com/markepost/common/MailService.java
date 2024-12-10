package com.markepost.common;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender javaMailSender;
	
	// 이메일 인증을 위한 username
	@Value("${spring.mail.username}")
	private String userName;
	
	// 인증용 랜덤 코드 만들기
	public String createRandCode(String data, int stringLength) {
		SecureRandom r = new SecureRandom();
		
		if (stringLength < 1) {
			throw new IllegalArgumentException("length must be a positive number.");
		}
		
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            sb.append( data.charAt(
            		r.nextInt(data.length())
            		));
        }
        
        return sb.toString();
	}
	
	// 이메일 전송하기
	public void mailSend(String toMail, String title, String content) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(userName); // 보내는 주소
			helper.setTo(toMail); // 받는 주소
			helper.setSubject(title); // 메일 제목
			helper.setText(content, true); // 메일 내용, html:true
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String createConfirmMailContent(String name, String confirmCode) {
		return "<h1> 안녕하세요! " + name + "님! </h1>"
				+ "<br>"
				+ name + "님의 인증 코드입니다."
				+ "<h2>" + confirmCode + "</h2>"
				+ "인증 화면에 해당 코드를 입력해주세요!";
	}
}
