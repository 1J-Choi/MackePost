package com.markepost.user.bo;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.markepost.user.entity.UserEntity;
import com.markepost.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBO {
	private final UserRepository userRepository;
	
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	public UserEntity getUserEntityByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public UserEntity addUser(String loginId, String password, String name, String email) {
		return userRepository.save(
				UserEntity.builder()
				.loginId(loginId)
				.password(password)
				.name(name)
				.email(email)
				.build());
	}
	
	public UserEntity getUser(String loginId, String password) {
		UserEntity user = userRepository.findByLoginId(loginId);
		
		if (!BCrypt.checkpw(password, user.getPassword())) {
			return null;
		}
		
		return user;
	}
}
