package com.markepost.comment.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubComment {
	private int id;
	private int postId;
	private int commentId;
	private int userId;
	private int content;
	private boolean isDeleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
