package com.markepost.report.bo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.comment.bo.CommentBO;
import com.markepost.comment.domain.Comment;
import com.markepost.comment.domain.SubComment;
import com.markepost.page.generic.Page;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;
import com.markepost.report.constant.ReportType;
import com.markepost.report.domain.Report;
import com.markepost.report.dto.ReportListDTO;
import com.markepost.report.mapper.ReportMapper;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportBO {
	private final ReportMapper reportMapper;
	private final UserBO userBO;
	private final PostBO postBO;
	private final CommentBO commentBO;
	
	public Report addReport(ReportType reportType, 
			int fkId, int boardId, String content, int userId) {
		Report report = new Report();
		report.setFkId(fkId);
		report.setReportType(reportType);
		report.setBoardId(boardId);
		report.setUserId(userId);
		report.setContent(content);
		
		reportMapper.insertReport(report);
		
		return report;
	}
	
	public Page<ReportListDTO> getReportListDTOs(int boardId, int page) {
		Page<ReportListDTO> reportListDTOs = new Page<>();
		reportListDTOs.setNowPage(page);
		reportListDTOs.setPageSize(10);
		reportListDTOs.setTotalCount(reportMapper.countByBoardId(boardId));
		List<Report> reports = reportMapper.findByBoardId(
				boardId, reportListDTOs.getPageSize(), reportListDTOs.getOffset());
		
		
		for(Report report : reports) {
			ReportListDTO reportListDTO = new ReportListDTO();
			reportListDTO.setReport(report);
			if(report.getReportType().equals(ReportType.POST)) {
				// 글 게시자 이름 찾기
				Post post = postBO.getPostById(report.getFkId());
				reportListDTO.setText(post.getSubject());
				UserEntity user = userBO.getUserEntityById(post.getUserId());
				reportListDTO.setWriteUserName(user.getName());
			} else if (report.getReportType().equals(ReportType.COMMENT)) {
				// 댓글 게시자 이름 찾기
				Comment comment = commentBO.getCommentByFkId(report.getFkId());
				reportListDTO.setText(comment.getContent());
				UserEntity user = userBO.getUserEntityById(comment.getUserId());
				reportListDTO.setWriteUserName(user.getName());
			} else if (report.getReportType().equals(ReportType.SUBCOMMENT)) {
				// 대댓글 게시자 이름 찾기
				SubComment subComment = commentBO.getSubCommentByFkId(report.getFkId());
				reportListDTO.setText(subComment.getContent());
				UserEntity user = userBO.getUserEntityById(subComment.getUserId());
				reportListDTO.setWriteUserName(user.getName());
			}
			UserEntity reportUser = userBO.getUserEntityById(report.getUserId()); 
			reportListDTO.setReportUserName(reportUser.getName());
			
			reportListDTOs.add(reportListDTO);
		}
		
		return reportListDTOs;
	}
}
