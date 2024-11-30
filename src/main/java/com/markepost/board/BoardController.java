package com.markepost.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.BoardDetailDTO;
import com.markepost.board.domain.SearchBoardDTO;
import com.markepost.page.generic.Page;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.PostSearchDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardBO boardBO;
	private final PostBO postBO;
	
	@GetMapping("/create-board-view")
	public String createBoard(HttpSession session) {
		if(session.getAttribute("userId") == null) {
			return "user/signIn";
		}
		return "board/createBoard";
	}
	
	@GetMapping("/search-board-view")
	public String searchBoard(Model model, 
			@RequestParam(value = "page", defaultValue = "1") int page) {
		List<SearchBoardDTO> searchBoards = boardBO.getSearchBoards("", page);
		int totalCount = boardBO.count(null);
		int totalPages = (int) Math.ceil((double) totalCount / 10);
		
		model.addAttribute("searchBoards", searchBoards);
		model.addAttribute("name", "");
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);
		
		return "board/searchBoard";
	}
	
	@GetMapping("/search")
	public String searchBoard(
			@RequestParam("name") String name, 
			@RequestParam(value = "page", defaultValue = "1") int page, 
			Model model) {
		List<SearchBoardDTO> searchBoards = boardBO.getSearchBoards(name, page);
		int totalCount = boardBO.count(name);
		int totalPages = (int) Math.ceil((double) totalCount / 10);
		
		model.addAttribute("searchBoards", searchBoards);
		model.addAttribute("name", name);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);
		return "board/searchBoard";
	}
	
	@GetMapping("/post-list-view")
	public String boardDetail(
			@RequestParam("boardId") int boardId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "tagId", required = false) Integer tagId,
			Model model, HttpSession session) {
		Integer userId = (Integer) session.getAttribute("userId");
		
		// TODO: suspend에 따른 접근 차단 기능
		
		BoardDetailDTO boardDetailDTO = boardBO.getBoardDetailDTOByBoardId(boardId, userId);
		Page<PostSearchDTO> posts = postBO.getSearchPost(boardId, tagId, page, null, null);
		
		model.addAttribute("boardDetailDTO", boardDetailDTO);
		model.addAttribute("posts", posts);
		model.addAttribute("searchString", "");
		model.addAttribute("nowDate", LocalDateTime.now());
		
		return "board/boardDetail";
	}
}
