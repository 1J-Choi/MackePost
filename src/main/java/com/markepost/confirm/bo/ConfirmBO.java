package com.markepost.confirm.bo;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.markepost.common.MailService;
import com.markepost.confirm.entity.ConfirmEntity;
import com.markepost.confirm.repository.ConfirmRepository;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmBO {
	private final ConfirmRepository confirmRepository;
	private final UserBO userBO;
	private final MailService mailService;
	
	public ConfirmEntity addConfirm(int userId) {
		// 이미 인증코드가 존재한다면 삭제하기
		ConfirmEntity confirm = confirmRepository.findById(userId).orElse(null);
		if(confirm != null) {
			confirmRepository.delete(confirm);
		}
		
		// 인증코드 보낼 이메일 검색해오기
		UserEntity user = userBO.getUserEntityById(userId);
		String email = user.getEmail();
		
		// 랜덤 인증코드 생성하기
		String eng_lower = "abcdefghijklmnopqrstuvwxyz";
        String eng_upper = eng_lower.toUpperCase();
        String number = "0123456789";
        // 랜덤을 생성할 대상 문자열 만든 뒤 전송
        String data = eng_lower + eng_upper + number;
		String confirmCode = mailService.createRandCode(data, 10);
		
		// 해당 데이터를 바탕으로 메일 보내기
		String content = mailService.createConfirmMailContent(user.getName(), confirmCode);
		mailService.mailSend(email, "MarkePost 인증코드 메일입니다.", content);
		
		// confirm 정보를 저장하기
		ConfirmEntity newConfirm = ConfirmEntity.builder()
				.userId(userId)
				.confirmCode(confirmCode)
				.createdAt(LocalDateTime.now())
				.build();
		
		return confirmRepository.save(newConfirm);
	}
}
