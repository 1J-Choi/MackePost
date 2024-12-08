package com.markepost.comment.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentTopDTO {
	// (대)댓글 내용
	private String content;
	
	// 이동시킬 게시글 id
	private int postId;
		
	// 등록된 시간
	private LocalDateTime createdAt;
}
