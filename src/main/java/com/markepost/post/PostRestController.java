package com.markepost.post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.markepost.report.bo.ReportBO;
import com.markepost.report.constant.ReportType;
import com.markepost.report.domain.Report;
import com.markepost.suspend.bo.SuspendBO;
import com.markepost.suspend.constant.SuspendType;
import com.markepost.suspend.entity.SuspendEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostRestController {
	private final PostBO postBO;
	private final ImageBO imageBO;
	private final SuspendBO suspendBO;
	
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
	@Operation(summary = "일반글 등록", 
			description = "일반글 입력값을 받아 글을 등록합니다. (게시글 작성 정지 상태일 시 차단)")
	@PostMapping("/create-normal")
	public Map<String, Object> createNormalPost(
			@Parameter(description = "등록할 게시판 ID")
			@RequestParam("boardId") int boardId, 
			@Parameter(description = "게시글의 태그 ID (없을 시 0으로 받음)")
			@RequestParam("tagId") int tagId, 
			@Parameter(description = "게시글의 제목")
			@RequestParam("subject") String subject,
			@Parameter(description = "게시글의 내용")
			@RequestParam("content") String content,
			@Parameter(description = "게시글 첨부 이미지", required = false)
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			HttpSession session) {
		// session 에서 userId, userLoginId 추출
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		Map<String, Object> result = new HashMap<>();
		// 게시판 정지 유저일 경우 return
		SuspendEntity suspend = suspendBO.getSuspend(userId, boardId, SuspendType.POST);
		LocalDateTime now = LocalDateTime.now();
		if(suspend != null && suspend.getUntillTime().compareTo(now) > 0 ) {
			// suspend.getUntillTime().compareTo(now)
			// suspend.getUntillTime() 가 now 보다 이후일 시 => 1
			// true가 되면 untillTime(정지 마감기간) 안이다!
			result.put("code", 200);
			result.put("error_message", "현재 게시글 작성 정지 상태입니다.");
			result.put("untilTime", suspend.getUntillTime());
			return result;
		}
		
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
	@Operation(summary = "거래글 등록", 
			description = "거래글 입력값을 받아 글을 등록합니다. (게시글 작성 정지 상태일시 차단)")
	@PostMapping("/create-market")
	public Map<String, Object> createMarketPost(
			@Parameter(description = "등록할 게시판 ID")
			@RequestParam("boardId") int boardId, 
			@Parameter(description = "게시글의 태그 ID (거래글의 경우 태그가 필수)")
			@RequestParam("tagId") int tagId, 
			@Parameter(description = "게시글의 제목")
			@RequestParam("subject") String subject,
			@Parameter(description = "상품명")
			@RequestParam("itemName") String itemName,
			@Parameter(description = "가격", required = false)
			@RequestParam(value = "price", required = false) Integer price,
			@Parameter(description = "상품 설명")
			@RequestParam("content") String content,
			@Parameter(description = "게시글 첨부 이미지", required = false)
			@RequestParam(value = "files", required = false) List<MultipartFile> files,
			HttpSession session) {
		// session 에서 userId, userLoginId 추출
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		Map<String, Object> result = new HashMap<>();
		// 게시판 정지 유저일 경우 return
		SuspendEntity suspend = suspendBO.getSuspend(userId, boardId, SuspendType.POST);
		LocalDateTime now = LocalDateTime.now();
		if(suspend != null && suspend.getUntillTime().compareTo(now) > 0 ) {
			// suspend.getUntillTime().compareTo(now)
			// suspend.getUntillTime() 가 now 보다 이후일 시 => 1
			// true가 되면 untillTime(정지 마감기간) 안이다!
			result.put("code", 200);
			result.put("error_message", "현재 게시글 작성 정지 상태입니다.");
			result.put("untilTime", suspend.getUntillTime());
			return result;
		}
		
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
	@Operation(summary = "거래완료 처리", 
			description = "작성자가 거래글을 거래 완료 처리합니다.")
	@PatchMapping("/market-end")
	public Map<String, Object> marketEnd(
			@Parameter(description = "게시글 ID")
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
	
	/**
	 * 게시글 수정
	 * @param postId
	 * @param subject
	 * @param content
	 * @param files
	 * @param itemName
	 * @param price
	 * @param session
	 * @return
	 */
	@Operation(summary = "게시글 수정", 
			description = "입력받은 값을 바탕으로 일반글or거래글의 내용을 수정합니다.")
	@PatchMapping("/update")
	public Map<String, Object> updatePost(
			@Parameter(description = "게시글 ID")
			@RequestParam("postId") int postId,
			@Parameter(description = "게시글 제목")
			@RequestParam("subject") String subject, 
			@Parameter(description = "게시글 내용 (거래글일 시 상품 설명)")
			@RequestParam("content") String content, 
			@Parameter(description = "게시글 이미지", required = false)
			@RequestParam(name = "files", required = false) List<MultipartFile> files, 
			@Parameter(description = "상품명", required = false)
			@RequestParam(name = "itemName", required = false) String itemName, 
			@Parameter(description = "가격", required = false)
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
	
	@Operation(summary = "게시글 삭제", 
			description = "게시글을 삭제 상태로 전환하고 화면에서 노출되지 않게합니다.")
	@PatchMapping("/delete")
	public Map<String, Object> deletePost(
			@Parameter(description = "게시글 ID")
			@RequestParam("postId") int postId, 
			HttpSession session) {
		// 접속한 유저에 의한것인지 확인용 (이후에 권한 설정 할 것)
		int userId = (int) session.getAttribute("userId");
		
		postBO.updatePostisDeleted(postId);
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
}
