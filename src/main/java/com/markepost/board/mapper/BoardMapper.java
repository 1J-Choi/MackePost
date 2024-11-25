package com.markepost.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.markepost.board.domain.Board;

@Mapper
public interface BoardMapper {
	public Board selectBoardByName(String name);
	public void insertBoard(Board board);
}
