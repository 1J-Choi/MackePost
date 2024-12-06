package com.markepost.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.like.entity.LikeEntity;
import com.markepost.like.entity.LikeId;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeId>{
	public int countByLikeIdPostId(int postId);
	public int countByLikeId(LikeId likeId);
	public void deleteByLikeIdPostId(int postId);
}
