package com.markepost.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.markepost.board.domain.Board;

@Mapper
public interface BoardMapper {
	public Board selectBoardByName(String name);
	public void insertBoard(Board board);
	public List<Board> selectBoardList(String name);
}
