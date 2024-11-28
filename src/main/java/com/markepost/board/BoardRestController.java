package com.markepost.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.Board;
import com.markepost.board.domain.SearchBoardDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardRestController {
	private final BoardBO boardBO;
	
	/**
	 * 게시판 이름 중복 확인
	 * @param name
	 * @return
	 */
	@GetMapping("/is-duplicate-name")
	public Map<String, Object> duplicateName(
			@RequestParam("name") String name) {
		Board board = boardBO.getBoardByName(name);
		
		Map<String, Object> result = new HashMap<>();
		if(board != null) {
			// 이름이 중복일 시
			result.put("code", 200);
			result.put("is_duplicate_name", true);
		} else {
			// 중복이 없을 시
			result.put("code", 200);
			result.put("is_duplicate_name", false);
		}
		return result;
	}
	
	@PostMapping("/create")
	public Map<String, Object> createBoard(
			@RequestParam("name") String name,
			@RequestParam("introduce") String introduce,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		// session 에서 userId(로그인 상태 확인용) 추출
		Integer userId = (Integer) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		Map<String, Object> result = new HashMap<>();
		if(userId == null) {
			result.put("code", 403);
			result.put("error_message", "로그인하신 뒤 게시판을 생성할 수 있습니다.");
			return result;
		}
		
		Board board = boardBO.addBoard(name, introduce, file, userId, userLoginId);
		
		if (board != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "게시판 등록 중 문제가 발생했습니다.");
		}
		return result;
	}
}
