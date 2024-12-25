package com.markepost.board;

import java.util.ArrayList;
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
import com.markepost.page.generic.Page;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.PostSearchDTO;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Tag(name = "Board API", description = "게시판 관련 API")
public class BoardRestController {
	private final BoardBO boardBO;
	private final UserBO userBO;
	private final PostBO postBO;
	
	/**
	 * 게시판 이름 중복 확인
	 * @param name
	 * @return
	 */
	@GetMapping("/is-duplicate-name")
	@Operation(summary = "게시판 이름 중복 확인", 
	description = "입력받은 게시판 이름이 이미 존재하는 게시판들의 이름과 중복되는지 확인힙니다. 이후 중복 여부 결과를 boolean을 반환합니다.")
	public Map<String, Object> duplicateName(
			@Parameter(description = "입력받은 게시판 이름")
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
	
	/**
	 * 게시판 생성
	 * @param name
	 * @param introduce
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	@Operation(summary = "게시판 생성", 
	description = "입력받은 값들을 바탕으로 게시판을 생성합니다.")
	public Map<String, Object> createBoard(
			@Parameter(description = "게시판 이름")
			@RequestParam("name") String name,
			@Parameter(description = "게시판 소개")
			@RequestParam("introduce") String introduce,
			@Parameter(description = "게시판 아이콘 이미지 (1개)", required = false)
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
