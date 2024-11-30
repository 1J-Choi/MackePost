package com.markepost.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
			@Param("contentText") String contentText,
			@Param("userIdList") List<Integer> userIdList);
	public List<Post> selectPostSearchDTO(
			@Param("boardId") int boardId, 
			@Param("tagId") Integer tagId, 
			@Param("contentText") String contentText,
			@Param("userIdList") List<Integer> userIdList, 
			@Param("pageSize") int pageSize, 
			@Param("offset") int offset);
}
