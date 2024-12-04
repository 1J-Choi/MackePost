package com.markepost.post.domain;

import java.util.List;

import com.markepost.comment.domain.CommentDTO;
import com.markepost.image.entity.ImageEntity;
import com.markepost.tag.entity.TagEntity;
import com.markepost.user.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDetailDTO {
	// Post 1개
	private Post post;
	
	// 글쓴이 1개
	private UserEntity user;
	
	// 글쓴이가 인증된 유저?
	private boolean isConfirmedUser;
	
	// 글쓴이가 게시판의 admin?
	private boolean isAdmin;
	
	// 글쓴이가 게시판의 main admin?
	private boolean isMain;

	// 태그 있을 시 Tag 1개
	private TagEntity tag;
	
	// 이미지 있을 시 List
	private List<ImageEntity> imageList;
	
	// MarketPost 있을 시 1개
	private MarketPost marketPost;
	
	// 좋아요 갯수
	private int likeCount;
	
	// 좋아요 여부
	private boolean filledLike;
	
	// 댓글/대댓글 리스트
	private List<CommentDTO> commentList;
}
