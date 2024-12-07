package com.markepost.report.dto;

import com.markepost.report.constant.ReportType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCreateDTO {
	// 리포트 종류
	private ReportType reportType;
	
	// 리포트 당하는 게시글/댓글/대댓글 id
	private int fkId;
	
	// 리포트 하는 게시글/댓글의 게시판 id
	private int boardId;
	
	// 리포트할 대상의 제목 or 댓글내용
	private String text;
	
	// 리포트 당하는 게시글/댓글/대댓글의 작성자 이름
	private String writeUserName;
	
	// 해당 리포트와 관련된 게시글 id
	// 게시글 리포트일시 fkId값과 똑같음
	// 게시글로 돌아가기 버튼용
	private int postId;
}
