package com.markepost.report.dto;

import com.markepost.report.domain.Report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportListDTO {
	// report 객체 1개
	private Report report;
	
	// 글 작성자 이름
	private String writeUserName;
	
	// 신고자 이름
	private String reportUserName;
	
	// 게시글 제목 or (대)댓글 내용
	private String text;
}
