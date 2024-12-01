package com.markepost.post;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.markepost.board.bo.BoardBO;
import com.markepost.tag.bo.TagBO;
import com.markepost.tag.domain.PostTagDTO;
import com.markepost.tag.entity.TagEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
	private final TagBO tagBO;
	private final BoardBO boardBO;
	
	@GetMapping("/create-post-view")
	public String createPost(
			@RequestParam("boardId") int boardId,
			Model model, HttpSession session) {
		//TODO: suspend에 따른 게시글 작성 차단
		String boardName = boardBO.getBoardById(boardId).getName();
		List<PostTagDTO> postTagDTOList = tagBO.getpostTagDTOListByBoardId(boardId);
		model.addAttribute("postTagDTOList", postTagDTOList);
		model.addAttribute("boardId", boardId);
		model.addAttribute("boardName", boardName);
		return "post/createPost";
	}
}