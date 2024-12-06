package com.markepost.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.image.bo.ImageBO;
import com.markepost.image.entity.ImageEntity;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.MarketPost;
import com.markepost.post.domain.Post;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostRestController {
	private final PostBO postBO;
	private final ImageBO imageBO;
	
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
	
	/**
	 * 거래글 생성
	 * @param boardId
	 * @param tagId
	 * @param subject
	 * @param itemName
	 * @param price
	 * @param content
	 * @param files
	 * @param session
	 * @return
	 */
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
	
	/**
	 * 거래완료 처리
	 * @param postId
	 * @param session
	 * @return
	 */
	@PatchMapping("/market-end")
	public Map<String, Object> marketEnd(
			@RequestParam("postId") int postId, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		int rowCount = postBO.updateMarketPostIsDone(postId, userId);
		Map<String, Object> result = new HashMap<>();
		if(rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "거래 완료 기능에 문제가 발생했습니다.");
		}
		
		return result;
	}
	
	@PatchMapping("/update")
	public Map<String, Object> updatePost(
			@RequestParam("postId") int postId, 
			@RequestParam("subject") String subject, 
			@RequestParam("content") String content, 
			@RequestParam(name = "files", required = false) List<MultipartFile> files, 
			@RequestParam(name = "itemName", required = false) String itemName, 
			@RequestParam(name = "price", required = false) Integer price, 
			HttpSession session) {
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		// 기본 속성(subjcet, content) 세팅
		// content의 경우 javaScript에서 각각 content로 받아왔기에 따로 분기할 필요 X
		Post post = postBO.getPostById(postId);
		List<ImageEntity> images = imageBO.getImageList(postId);
		post.setSubject(subject);
		post.setContent(content);
		int rowCount = postBO.updatePost(post, images, files, userLoginId);
		
		int marketrowCount = -1;
		if(itemName != null) { // 상품명이 있을 시 => 무조건 거래글
			MarketPost marketPost = postBO.getMarketPost(postId);
			marketPost.setItemName(itemName);
			marketPost.setPrice(price);
			marketrowCount = postBO.updateMarketPost(marketPost);
		}
		
		Map<String, Object> result = new HashMap<>();
		if((rowCount > 0 && marketrowCount == -1) || (rowCount > 0 && marketrowCount > 0)) {
			// 일반글 부분만 수정됬거나(일반글) 일반글, 거래글 부분 모두 수정되었을 때(거래글)
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			// 오류
			result.put("code", 400);
			result.put("error_message", "게시글 수정에 실패하였습니다.");
		}
		
		return result;
	}
}
