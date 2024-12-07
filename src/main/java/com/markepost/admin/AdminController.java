package com.markepost.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.BoardDetailDTO;
import com.markepost.page.generic.Page;
import com.markepost.report.bo.ReportBO;
import com.markepost.report.domain.Report;
import com.markepost.report.dto.ReportDetailDTO;
import com.markepost.report.dto.ReportListDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final BoardBO boardBO;
	private final ReportBO reportBO;
	
	@GetMapping("/board/board-setting-view")
	public String boardSettingView(
			@RequestParam("boardId") int boardId, 
			Model model, HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		BoardDetailDTO boardDetailDTO = boardBO.getBoardDetailDTOByBoardId(boardId, userId);
		if(!boardDetailDTO.isAdmin()) { // admin이 아닌 유저일 경우
			return "user/signIn";
		}
		model.addAttribute("boardDetailDTO", boardDetailDTO);
		return "admin/boardSetting";
	}
	
	@GetMapping("/report/report-list-view")
	public String reportList(
			@RequestParam("boardId") int boardId, 
			@RequestParam(name = "page", defaultValue = "1") int page,
			Model model) {
		Page<ReportListDTO> reportListDTOs = reportBO.getReportListDTOs(boardId, page);
		model.addAttribute("reportListDTOs", reportListDTOs);
		model.addAttribute("boardId", boardId);
		
		return "admin/reportList";
	}
	
	@GetMapping("/report/report-detail-view")
	public String reportDetail(
			@RequestParam("reportId") int reportId, 
			Model model) {
		ReportDetailDTO reportDetailDTO = reportBO.getReportDetailDTO(reportId);
		
		model.addAttribute("reportDetailDTO", reportDetailDTO);
		
		return "admin/reportDetail";
	}
}
