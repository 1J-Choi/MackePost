package com.markepost.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBoardDTO {
	// 게시판 1개
	private Board board;
	
	// 인게임 거래 태그 유무
	private boolean hasIngameTag;
	
	// 실물 거래 태그 유무
	private boolean hasRealTag;
}
