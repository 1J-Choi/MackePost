package com.markepost.like;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.like.bo.LikeBO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Like API", description = "좋아요 관련 API")
public class LikeRestController {
	private final LikeBO likeBO;
	
	@Operation(summary = "좋아요 토글", 
			description = "입력받은 게시글 아이디와 session의 userId를 바탕으로 좋아요를 생성하거나 제거합니다.")
	@GetMapping("/like/{postId}")
	public Map<String, Object> likeToggle(
			@Parameter(description = "게시글 ID")
			@PathVariable(name = "postId") int postId, 
			HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null) {
			result.put("code", 403);
			result.put("error_message", "로그인 한 뒤 좋아요를 누를 수 있습니다.");
			return result;
		}
		
		likeBO.toggleLike(postId, userId);
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
}
