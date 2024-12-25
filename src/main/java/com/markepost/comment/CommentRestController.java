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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "댓글 관련 API")
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
	@Operation(summary = "(대)댓글 등록", 
	description = "입력값을 바탕으로 댓글을 등록하며 upperCommentId가 있을 시 대댓글로 등록합니다. (댓글 작성 정시 상태일 시 차단)")
	public Map<String, Object> createComment(
			@Parameter(description = "게시글 ID")
			@RequestParam("postId") int postId, 
			@Parameter(description = "댓글 내용")
			@RequestParam("content") String content,
			@Parameter(description = "상위 댓글 ID", required = false)
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
	@Operation(summary = "(대)댓글 삭제", 
	description = "commentType를 바탕으로 댓글/대댓글을 구분하여 이를 삭제합니다.")
	public Map<String, Object> deleteComment(
			@Parameter(description = "댓글 타입 (COMMENT, SUBCOMMENT)")
			@RequestParam("commentType")String commentType, 
			@Parameter(description = "(대)댓글 ID")
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
