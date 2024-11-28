package com.markepost.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.admin.entity.AdminEntity;
import com.markepost.board.bo.BoardBO;
import com.markepost.tag.bo.TagBO;
import com.markepost.tag.entity.TagEntity;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import groovy.lang.Delegate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {
	private final BoardBO boardBO;
	private final TagBO tagBO;
	private final UserBO userBO;
	private final AdminBO adminBO;

	/**
	 * 게시판 수정
	 * @param boardId
	 * @param introduce
	 * @param file
	 * @param session
	 * @return
	 */
	@PatchMapping("/board/update")
	public Map<String, Object> boardUpdate(
			@RequestParam("boardId") int boardId, 
			@RequestParam("introduce") String introduce,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		// userId 받아오기
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		int rowCount = boardBO.updateBoard(boardId, introduce, file, userId, userLoginId);
		Map<String, Object> result = new HashMap<>();
		if(rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "게시판 정보를 수정하는데 문제가 발생하였습니다.");
		}
		return result;
	}
	
	/**
	 * 태그 생성
	 * @param boardId
	 * @param tagName
	 * @param tagTypeString
	 * @return
	 */
	@PostMapping("/board/create-tag")
	public Map<String, Object> createTag(
			@RequestParam("boardId") int boardId, 
			@RequestParam("tagName") String tagName, 
			@RequestParam("tagTypeString") String tagTypeString) {
		TagEntity tag = tagBO.addTag(boardId, tagName, tagTypeString);
		
		Map<String, Object> result = new HashMap<>();
		if(tag != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "이미 존재하는 이름의 태그입니다.");
		}
		
		return result;
	}
	
	/**
	 * 어드민 ID 조회
	 * @param adminLoginId
	 * @param boardId
	 * @return
	 */
	@GetMapping("/user/search-by-loginid")
	public Map<String, Object> searchByLoginId(
			@RequestParam("adminLoginId") String adminLoginId, 
			@RequestParam("boardId") int boardId) {
		Map<String, Object> result = new HashMap<>();
		
		// 유저 존재 유무
		UserEntity exUser = userBO.getUserEntityByLoginId(adminLoginId);
		// 해당 아이디를 가진 유저가 없을 경우
		if(exUser == null) {
			result.put("code", 200);
			result.put("is_existed_loginId", false);
			return result;
		}
		
		// 게시판에 중복된 admin 존재 유무 => 중복된다 true, 중복 안됨 false
		boolean hasAdmin = adminBO.existsByboardIdAndUserId(boardId, exUser.getId());
		result.put("code", 200);
		result.put("is_existed_loginId", true);
		result.put("is_duplicated_loginId", hasAdmin);
		return result;
	}
	
	/**
	 * subAmdin 등록
	 * @param adminLoginId
	 * @param boardId
	 * @return
	 */
	@PostMapping("/board/create-subadmin")
	public Map<String, Object> addSubAdmin(
			@RequestParam("adminLoginId") String adminLoginId, 
			@RequestParam("boardId") int boardId,
			HttpSession session) {
		// mainAdmin이 삭제하는 것인지 체크하기 위해 session의 userId 추출
		int nowUserId = (int) session.getAttribute("userId");
		AdminEntity nowAdmin = adminBO.getAdminEntityById(boardId, nowUserId);
		
		Map<String, Object> result = new HashMap<>();
		if(!nowAdmin.isAdminType()) {
			result.put("code", 403);
			result.put("error_message", "관리자 등록은 메인 관리지만 가능합니다.");
			return result;
		}
		
		UserEntity user = userBO.getUserEntityByLoginId(adminLoginId);
		adminBO.createAdmin(boardId, user.getId(), false);
		
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	@DeleteMapping("/board/delete-subadmin")
	public Map<String, Object> deleteSubAdmin(
			@RequestParam("boardId") int boardId, 
			@RequestParam("userId") int userId, 
			HttpSession session) {
		// mainAdmin이 삭제하는 것인지 체크하기 위해 session의 userId 추출
		int nowUserId = (int) session.getAttribute("userId");
		AdminEntity nowAdmin = adminBO.getAdminEntityById(boardId, nowUserId);
		
		Map<String, Object> result = new HashMap<>();
		if(!nowAdmin.isAdminType()) {
			result.put("code", 403);
			result.put("error_message", "관리자 삭제는 메인 관리지만 가능합니다.");
			return result;
		}
		
		adminBO.deleteAdminEntity(boardId, userId);
		
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
}
