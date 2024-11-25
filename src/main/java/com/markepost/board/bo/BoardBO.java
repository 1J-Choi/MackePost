package com.markepost.board.bo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.board.domain.Board;
import com.markepost.board.mapper.BoardMapper;
import com.markepost.common.FileManagerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardBO {
	private final BoardMapper boardMapper;
	private final AdminBO adminBO;
	private final FileManagerService fileManager;
	
	public Board getBoardByName(String name) {
		return boardMapper.selectBoardByName(name);
	}
	
	public Board addBoard(String name, String introduce, 
			MultipartFile file, int userId, String userLoginId) {
		// 게시판 프로필 이미지 폴더명의 경우 loginId + "_board"의 형태를 가지게 함
		String imagePath = null;
		if(file != null) {
			imagePath = fileManager.uploadFile(file, userLoginId + "_board");
		}
		
		Board board = new Board();
		board.setName(name);
		board.setIntroduce(introduce);
		board.setImagePath(imagePath);
		
		// 게시판 db insert
		boardMapper.insertBoard(board);
		
		// 만들어진 게시판의 admin을 생성한 user로 설정
		// mainAdmin이기 때문에 adminType을 true로 한다
		adminBO.createAdmin(userId, userId, true);
		
		return board;
	}
}
