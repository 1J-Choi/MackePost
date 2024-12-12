package com.markepost.user.dto;

import com.markepost.point.entity.PointEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
	// 유저 id
	private int id;
	
	// 유저 로그인 id
	private String loginId;
	
	// 유저 닉네임
	private String name;
	
	// 유저 자기소개
	private String introduce;
	
	// 유저 프로필 이미지 주소
	private String imagePath;
	
	// 유저 인증 여부
	private boolean isConfirmed;
	
	// 유저 point 객체 1개
	// 본인일 시 && 인증완료 됬을 시에만 가져온다
	private PointEntity point;
}
