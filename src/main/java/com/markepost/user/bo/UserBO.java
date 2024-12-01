package com.markepost.user.bo;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.common.FileManagerService;
import com.markepost.user.entity.UserEntity;
import com.markepost.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserBO {
	private final UserRepository userRepository;
	private final FileManagerService fileManager;
	
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
	
	public UserEntity getUserEntityById(int userId) {
		return userRepository.findById(userId).orElse(null);
	}
	
	public UserEntity updateUser(int userId, String userLoginId, 
			String name, String introduce, MultipartFile file) {
		UserEntity user = userRepository.findById(userId).orElse(null);
		if(user == null) {
			return null;
		}
		user.setName(name);
		user.setIntroduce(introduce);
		
		// 이미지 파일을 올린 경우
		// 이미지 파일 포함 update
		String imagePath = null;
		if(file != null) {
			imagePath = fileManager.uploadFile(file, userLoginId);
			if(imagePath != null && user.getImagePath() != null) {
				// 폴더, 이미지 제거(컴퓨터-서버)
				fileManager.deleteFile(user.getImagePath());
			}
			user.setImagePath(imagePath);
		}
		
		userRepository.save(user);
		return user;
	}
	
	public List<UserEntity> getUserListByLikeName(String name) {
		return userRepository.findByNameLike("%" + name + "%");
	}
}
