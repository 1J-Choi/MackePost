package com.markepost.board.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.admin.entity.AdminDto;
import com.markepost.board.domain.Board;
import com.markepost.board.domain.BoardDetailDTO;
import com.markepost.board.domain.SearchBoardDTO;
import com.markepost.board.mapper.BoardMapper;
import com.markepost.common.FileManagerService;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;
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
	
	public Board getBoardById(int boardId) {
		return boardMapper.selectBoardById(boardId);
	}
	
	public Board addBoard(String name, String introduce, 
			MultipartFile file, int userId, String userLoginId) {
		// 게시판 프로필 이미지 폴더명의 경우 boardName + "_board"의 형태를 가지게 함
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
		adminBO.createAdmin(board.getId(), userId, true);
		
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
	
	public BoardDetailDTO getBoardDetailDTOByBoardId(int boardId, Integer userId) {
		BoardDetailDTO boardDetailDTO = new BoardDetailDTO();
		Board board = boardMapper.selectBoardById(boardId);
		List<AdminDto> adminDtos = adminBO.getAdminByBoardId(boardId);
		boolean isAdmin = adminBO.existsByboardIdAndUserId(boardId, userId);
		List<TagEntity> tags = tagBO.getTagListByBoardId(boardId);
		// 나중에 구현할 postDTO 받아오기
		// List<PostDTO> posts = postBO.getPostListByBoardIdAndPage(int boardId, int page);
		
		boardDetailDTO.setBoard(board);
		boardDetailDTO.setAdminDtos(adminDtos);
		boardDetailDTO.setTags(tags);
		boardDetailDTO.setAdmin(isAdmin);
		// is로 시작하는 boolean의 경우 메소드 명에서 is가 생략된다!
		//boardDetailDTO.setPosts(posts);
		
		return boardDetailDTO;
	}
	
	public int updateBoard(int boardId, String introduce, 
			MultipartFile file, int userId, String userLoginId) {
		Board board = boardMapper.selectBoardById(boardId);
		if(board == null) {
			return 0;
		}
		
		String imagePath = null;
		if(file != null) {
			// 새 프로필 이미지 업로드
			imagePath = fileManager.uploadFile(file, userLoginId + "_board");
			// 업로드 성공 시 기존 이미지 존재하면 삭제
			if(imagePath != null && board.getImagePath() != null) {
				// 이미지, 폴더 삭제하기
				fileManager.deleteFile(board.getImagePath());
			}
		}
		
		//DB update
		return boardMapper.updateBoardByPostId(boardId, introduce, imagePath);
	}
	
	public List<SearchBoardDTO> getTop5BoardList() {
		List<SearchBoardDTO> top5BoardList = new ArrayList<>();
		List<Board> top5Boards = boardMapper.selectTop5Boards();
		for(Board board : top5Boards) {
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
			top5BoardList.add(searchBoardDTO);
		}
		
		return top5BoardList;
	}
}
