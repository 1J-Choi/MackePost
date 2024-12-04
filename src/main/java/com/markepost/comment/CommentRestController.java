package com.markepost.comment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.comment.bo.CommentBO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {
	private final CommentBO commentBO;
	
	@PostMapping("/create")
	public Map<String, Object> createComment(
			@RequestParam("postId") int postId, 
			@RequestParam("content") String content, 
			@RequestParam(name = "upperCommentId", required = false) Integer upperCommentId, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		Map<String, Object> result = new HashMap<>();
		// TODO: suspend에 따른 댓글 기능 제한
		
		int rowCount = commentBO.addComment(postId, content, upperCommentId, userId);
		if(rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "댓글 등록에 실패했습니다.");
		}
		return result;
	}
}
