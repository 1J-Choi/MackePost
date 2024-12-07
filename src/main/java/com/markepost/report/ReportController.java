package com.markepost.report;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.Board;
import com.markepost.comment.bo.CommentBO;
import com.markepost.comment.domain.Comment;
import com.markepost.comment.domain.SubComment;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;
import com.markepost.report.constant.ReportType;
import com.markepost.report.dto.ReportCreateDTO;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
	private final PostBO postBO;
	private final UserBO userBO;
	private final CommentBO commentBO;
	
	@GetMapping("/create-report-view")
	public String createReport(
			@RequestParam("reportType") String reportType, 
			@RequestParam("fkId") int fkId, 
			Model model) {
		ReportCreateDTO reportCreateDTO = new ReportCreateDTO();
		reportCreateDTO.setFkId(fkId);
		
		if (reportType.equals("POST")) { // 게시글 신고
			Post post = postBO.getPostById(fkId);
			reportCreateDTO.setText(post.getSubject());
			reportCreateDTO.setBoardId(post.getBoardId());
			UserEntity user = userBO.getUserEntityById(post.getUserId());
			reportCreateDTO.setWriteUserName(user.getName());
			reportCreateDTO.setPostId(fkId); // postId와 fkId가 동일
			
		} else if (reportType.equals("COMMENT")) { // 댓글 신고
			Comment comment = commentBO.getCommentByFkId(fkId);
			reportCreateDTO.setText(comment.getContent());
			Post post = postBO.getPostById(comment.getPostId());
			reportCreateDTO.setBoardId(post.getBoardId());
			UserEntity user = userBO.getUserEntityById(comment.getUserId());
			reportCreateDTO.setWriteUserName(user.getName());
			reportCreateDTO.setPostId(comment.getPostId());
			
		} else if (reportType.equals("SUBCOMMENT")) { // 유저 신고
			SubComment subComment = commentBO.getSubCommentByFkId(fkId);
			reportCreateDTO.setText(subComment.getContent());
			Post post = postBO.getPostById(subComment.getPostId());
			reportCreateDTO.setBoardId(post.getBoardId());
			UserEntity user = userBO.getUserEntityById(subComment.getUserId());
			reportCreateDTO.setWriteUserName(user.getName());
			reportCreateDTO.setPostId(subComment.getPostId());
		} else {
			return "/";
		}
		
		reportCreateDTO.setReportType(ReportType.valueOf(reportType));
		
		model.addAttribute("reportCreateDTO", reportCreateDTO);
		return "report/createReport";
	}
}
