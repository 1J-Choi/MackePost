package com.markepost.report.domain;

import java.time.LocalDateTime;

import com.markepost.report.constant.ReportType;

import lombok.Data;

@Data
public class Report {
	private int id;
	private int fkId;
	private ReportType reportType;
	private int boardId;
	private int userId;
	private String content;
	private LocalDateTime createdAt;
}
