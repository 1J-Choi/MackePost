package com.markepost.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.BoardDetailDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final BoardBO boardBO;
	
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
}
