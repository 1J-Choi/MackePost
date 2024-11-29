package com.markepost.post.mapper;

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
}
