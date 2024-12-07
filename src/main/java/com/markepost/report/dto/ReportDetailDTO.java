package com.markepost.report.dto;

import com.markepost.report.domain.Report;
import com.markepost.user.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDetailDTO {
	// report 1개
	private Report report;
	
	// 해당 글 or 댓글 달린 글 id
	private int postId;
	
	// 해당 글의 제목 or 댓글의 내용
	private String text;
	
	// 작성자
	private UserEntity writeUser;
	
	// 신고자
	private UserEntity reportUser;
	
	// 게시판 id
	private int boardId;
}
