package com.markepost.post.domain;

import java.time.LocalDateTime;

import com.markepost.tag.entity.TagEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchDTO {
	// post 1개 (postId, 글쓴이 Name & userId & 인증/admin여부(main sub), subject, tagId, createdAt)
	private int postId;
	private TagEntity tag;
	private String subject;
	private int writerId;
	private String writerName;
	private boolean isConfirmedUser;
	private boolean isAdmin;
	private boolean isMain;
	private LocalDateTime createdAt;
	
	// 좋아요 갯수
	private int likeCount;
}
