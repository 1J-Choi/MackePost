package com.markepost.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostRestController {
	private final PostBO postBO;
	
	/**
	 * 일반글 업로드
	 * @param boardId
	 * @param tagId
	 * @param subject
	 * @param content
	 * @param files
	 * @param session
	 * @return
	 */
	@PostMapping("/create-normal")
	public Map<String, Object> createNormalPost(
			@RequestParam("boardId") int boardId, 
			@RequestParam("tagId") int tagId, 
			@RequestParam("subject") String subject, 
			@RequestParam("content") String content,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			HttpSession session) {
		// session 에서 userId, userLoginId 추출
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		Map<String, Object> result = new HashMap<>();
		// 게시판 정지 유저일 경우 return
		
		// db insert
		Post post = postBO.addNormalPost(boardId, tagId, subject, content, files, userId, userLoginId);
		
		// return
		if(post != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 403);
			result.put("error_message", "일반 게시글을 등록하는데 실패했습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/create-market")
	public Map<String, Object> createMarketPost(
			@RequestParam("boardId") int boardId, 
			@RequestParam("tagId") int tagId, 
			@RequestParam("subject") String subject,
			@RequestParam("itemName") String itemName,
			@RequestParam(value = "price", required = false) Integer price,
			@RequestParam("content") String content,
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			HttpSession session) {
		// session 에서 userId, userLoginId 추출
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		Map<String, Object> result = new HashMap<>();
		// 게시판 정지 유저일 경우 return
		
		// db insert
		Post post = postBO.addMarketPost(
				boardId, tagId, subject, 
				itemName, price, content, 
				files, userId, userLoginId);
		
		// return
		if(post != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 403);
			result.put("error_message", "일반 게시글을 등록하는데 실패했습니다.");
		}
				
		return result;
	}
}
