package com.markepost.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.markepost.post.domain.MarketPost;
import com.markepost.post.domain.Post;

@Mapper
public interface PostMapper {
	public void insertNormalPost(Post post);
	public void insertMarketPost(
			@Param("postId") int postId,
			@Param("itemName") String itemName, 
			@Param("price") Integer price, 
			@Param("isDone") boolean isDone);
	public int count(
			@Param("boardId") int boardId, 
			@Param("tagId") Integer tagId,
			@Param("subjectText") String contentText,
			@Param("userIdList") List<Integer> userIdList);
	public List<Post> selectPostSearchDTO(
			@Param("boardId") int boardId, 
			@Param("tagId") Integer tagId, 
			@Param("subjectText") String subjectText,
			@Param("userIdList") List<Integer> userIdList, 
			@Param("pageSize") int pageSize, 
			@Param("offset") int offset);
	public Post selectPostById(int postId);
	public MarketPost getMarketPostById(int postId);
	public int updateMarketPostIsDone(
			@Param("postId") int postId, 
			@Param("userId") int userId);
}
