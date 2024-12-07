package com.markepost.post.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Post {
	private int id;
	private int boardId;
	private int userId;
	private Integer tagId;
	private String subject;
	private String content;
	private boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
