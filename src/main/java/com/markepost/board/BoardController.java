package com.markepost.board;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.SearchBoardDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardBO boardBO;
	
	@GetMapping("/create-board-view")
	public String createBoard(HttpSession session) {
		if(session.getAttribute("userId") == null) {
			return "user/signIn";
		}
		return "board/createBoard";
	}
	
	@GetMapping("/search-board-view")
	public String searchBoard(Model model) {
		List<SearchBoardDTO> searchBoards = boardBO.getSearchBoards(null);
		model.addAttribute("searchBoards", searchBoards);
		model.addAttribute("name", "");
		return "board/searchBoard";
	}
	
	@GetMapping("/search")
	public String searchBoard(
			@RequestParam(value = "name", required = false) String name, 
			@RequestParam(value = "page", required = false) Integer page, 
			Model model) {
		List<SearchBoardDTO> searchBoards = boardBO.getSearchBoards(name);
		model.addAttribute("searchBoards", searchBoards);
		model.addAttribute("name", name);
		return "board/searchBoard";
	}
}
