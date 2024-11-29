package com.markepost.post.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MarketPost {
	private int postId;
	private String itemName;
	private int price;
	private boolean isDone;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
