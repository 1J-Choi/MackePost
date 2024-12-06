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
}
