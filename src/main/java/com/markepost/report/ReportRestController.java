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

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportRestController {
	private final ReportBO reportBO;
	
	@PostMapping("/create")
	public Map<String, Object> createReport(
			@RequestParam("reportType") ReportType reportType, 
			@RequestParam("fkId") int fkId,
			@RequestParam("boardId") int boardId,
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
