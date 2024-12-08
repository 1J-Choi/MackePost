package com.markepost.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.markepost.comment.domain.Comment;
import com.markepost.comment.domain.SubComment;

@Mapper
public interface CommentMapper {
	public int insertComment(
			@Param("postId") int postId,
			@Param("content") String content, 
			@Param("userId") int userId);
	public int insertSubComment(
			@Param("postId") int postId,
			@Param("content") String content,
			@Param("upperCommentId") int upperCommentId,
			@Param("userId") int userId);
	public List<Comment> getCommentListByPostId(int postId);
	public List<SubComment> getSubCommentListByCommentId(int commentId);
	public int updateCommentDeleted(int commentId);
	public int updateSubCommentDeleted(int subCommentId);
	public void deleteCommentByPostId(int postId);
	public void deleteSubCommentByPostId(int postId);
	public Comment selectCommentById(int id);
	public SubComment selectSubCommentById(int id);
	// 사실 Comment와 SubComment 2개의 테이블에서 받아오는 것이지만
	// SubComment의 CommentId값을 빼고 조회할 것이며
	// 이는 Comment의 구조와 일치함으로 List<Comment>로 받는다.
	public List<Comment> selectCommentTopListByUserId(Integer userId);
}
