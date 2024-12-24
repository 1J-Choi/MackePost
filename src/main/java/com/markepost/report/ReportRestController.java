package com.markepost.report;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.report.bo.ReportBO;
import com.markepost.report.constant.ReportType;
import com.markepost.report.domain.Report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Tag(name = "Report API", description = "신고 관련 API")
public class ReportRestController {
	private final ReportBO reportBO;
	
	@PostMapping("/create")
	@Operation(summary = "신고 등록", 
	description = "신고하고자 하는 댓글or게시글의 id와 게시판id, 신고사유를 입력받아 등록합니다.")
	public Map<String, Object> createReport(
			@Parameter(description = "신고할 대상의 타입 (게시글, 댓글)")
			@RequestParam("reportType") ReportType reportType, 
			@Parameter(description = "신고할 대상(게시글, 댓글)의 ID")
			@RequestParam("fkId") int fkId,
			@Parameter(description = "신고할 대상이 있는 게시판 ID")
			@RequestParam("boardId") int boardId,
			@Parameter(description = "신고 사유")
			@RequestParam("content") String content, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		Report report = reportBO.addReport(reportType, fkId, boardId, content, userId);
		Map<String, Object> result = new HashMap<>();
		if(report != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "신고 등록 중 문제가 발생했습니다.");
		}
		return result;
	}
}
