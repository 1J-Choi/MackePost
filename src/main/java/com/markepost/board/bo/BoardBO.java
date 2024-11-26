package com.markepost.board.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.board.domain.Board;
import com.markepost.board.domain.SearchBoardDTO;
import com.markepost.board.mapper.BoardMapper;
import com.markepost.common.FileManagerService;
import com.markepost.tag.bo.TagBO;
import com.markepost.tag.constant.TagType;
import com.markepost.tag.entity.TagEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardBO {
	private final BoardMapper boardMapper;
	private final AdminBO adminBO;
	private final TagBO tagBO;
	private final FileManagerService fileManager;
	private static final int PAGE_SIZE = 10;
	
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
	
	public List<SearchBoardDTO> getSearchBoards(String name, int page) {
		List<SearchBoardDTO> searchBoardDTOs = new ArrayList<>();
		int offset = (page - 1) * PAGE_SIZE;
		List<Board> searchBoard = boardMapper.selectBoardList(name, PAGE_SIZE, offset);
		// 검색어가 없을 때 (초기 화면)
		for(Board board : searchBoard) {
			SearchBoardDTO searchBoardDTO = new SearchBoardDTO();
			searchBoardDTO.setBoard(board);
			
			// 인게임,실물 거래 태그 유무 찾기
			searchBoardDTO.setHasIngameTag(false);
			searchBoardDTO.setHasRealTag(false);
			List<TagEntity> tags = tagBO.getTagListByBoardId(board.getId());
			for(TagEntity tag : tags) {
				if (tag.getTagType().equals(TagType.INGAME)) {
					searchBoardDTO.setHasIngameTag(true);
					continue;
				}
				if (tag.getTagType().equals(TagType.REAL)) {
					searchBoardDTO.setHasRealTag(true);
					continue;
				}
			}
			searchBoardDTOs.add(searchBoardDTO);
		}
		
		return searchBoardDTOs;
	}
	
	public int count(String name) {
        return boardMapper.countBoards(name);
    }
}
