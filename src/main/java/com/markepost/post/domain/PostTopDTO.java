package com.markepost.post.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostTopDTO {
	// post 이름
	private String postName;
	
	// 이동시킬 post id
	private int postId;
	
	// 게시판 이름
	private String boardName;
	
	// 등록 날짜
	private LocalDateTime createdAt;
}
