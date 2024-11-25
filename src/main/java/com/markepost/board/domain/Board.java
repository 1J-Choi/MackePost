package com.markepost.board.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Board {
	private int id;
	private String name;
	private String introduce;
	private String imagePath;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
