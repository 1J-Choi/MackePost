package com.markepost.comment.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {
	private int id;
	private int postId;
	private int userId;
	private String content;
	private boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
