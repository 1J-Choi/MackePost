package com.markepost.comment;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.comment.bo.CommentBO;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;
import com.markepost.suspend.bo.SuspendBO;
import com.markepost.suspend.constant.SuspendType;
import com.markepost.suspend.entity.SuspendEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {
	private final CommentBO commentBO;
	private final SuspendBO suspendBO;
	private final PostBO postBO;
	
	/**
	 * (대)댓글 등록
	 * @param postId
	 * @param content
	 * @param upperCommentId
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> createComment(
			@RequestParam("postId") int postId, 
			@RequestParam("content") String content, 
			@RequestParam(name = "upperCommentId", required = false) Integer upperCommentId, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		Map<String, Object> result = new HashMap<>();
		// suspend에 따른 댓글 기능 제한
		Post post = postBO.getPostById(postId);
		SuspendEntity suspend = suspendBO.getSuspend(userId, post.getBoardId(), SuspendType.COMMENT);
		LocalDateTime now = LocalDateTime.now();
		if(suspend != null && suspend.getUntillTime().compareTo(now) > 0 ) {
			// suspend.getUntillTime().compareTo(now)
			// suspend.getUntillTime() 가 now 보다 이후일 시 => 1
			// true가 되면 untillTime(정지 마감기간) 안이다!
			result.put("code", 200);
			result.put("error_message", "현재 댓글 작성 정지 상태입니다.");
			result.put("untilTime", suspend.getUntillTime());
			return result;
		}
		
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
	
	@PatchMapping("/delete")
	public Map<String, Object> deleteComment(
			@RequestParam("commentType")String commentType, 
			@RequestParam("id") int id) {
		int rowCount = commentBO.updateCommentDeleted(commentType, id);
		
		Map<String, Object> result = new HashMap<>();
		if (rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "댓글을 삭제하는데 문제가 발생했습니다.");
		}
		return result;
	}
}
