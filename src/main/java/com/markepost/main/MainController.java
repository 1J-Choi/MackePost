package com.markepost.main;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.markepost.board.bo.BoardBO;
import com.markepost.board.domain.SearchBoardDTO;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.Post;
import com.markepost.post.domain.PostSearchDTO;
import com.markepost.post.domain.PostTopDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final BoardBO boardBO;
	private final PostBO postBO;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		List<SearchBoardDTO> top5BoardList = boardBO.getTop5BoardList();
		List<PostTopDTO> top5PostList = postBO.getTop5PostList(null);
		
		model.addAttribute("top5BoardList", top5BoardList);
		model.addAttribute("top5PostList", top5PostList);
		
		return "main/main";
	}
}
