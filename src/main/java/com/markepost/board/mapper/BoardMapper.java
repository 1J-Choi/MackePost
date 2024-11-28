package com.markepost.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.markepost.board.domain.Board;

@Mapper
public interface BoardMapper {
	public Board selectBoardByName(String name);
	public void insertBoard(Board board);
	public List<Board> selectBoardList(
			@Param("name") String name, 
			@Param("pageSize") int pageSize, 
			@Param("offset") int offset);
	public int countBoards(String name);
	public Board selectBoardById(int boardId);
	public int updateBoardByPostId(
			@Param("boardId")int boardId, 
			@Param("introduce") String introduce, 
			@Param("imagePath") String imagePath);
}
