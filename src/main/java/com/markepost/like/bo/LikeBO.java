package com.markepost.like.bo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.markepost.like.entity.LikeEntity;
import com.markepost.like.entity.LikeId;
import com.markepost.like.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeBO {
	private final LikeRepository likeRepository;
	
	public void toggleLike(int postId, int userId) {
		LikeId likeId = new LikeId(postId, userId);
		LikeEntity like = likeRepository.findById(likeId).orElse(null);
		if(like != null) { // like가 있다면 => 삭제
			likeRepository.delete(like);
		} else { // like가 없다면 => 등록
			like = new LikeEntity(likeId, LocalDateTime.now());
			likeRepository.save(like);
		}
	}
	
	public int getLikeCount(int postId) {
		return likeRepository.countByLikeIdPostId(postId);
	}
	
	public boolean getFilledLike(int postId, Integer userId) {
		if(userId == null) {
			return false;
		}
		
		LikeId likeId = new LikeId(postId, userId);
		return likeRepository.countByLikeId(likeId) > 0; 
	}
}
