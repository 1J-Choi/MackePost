package com.markepost.board.domain;

import java.util.List;

import com.markepost.admin.entity.AdminDto;
import com.markepost.admin.entity.AdminEntity;
import com.markepost.tag.entity.TagEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDetailDTO {
	// board 1개
	private Board board;
	
	// List<admin>
	private List<AdminDto> adminDtos;
	
	// session에 있는 유저가 어드민인가?
	private boolean isAdmin;
	
	// List<tag>
	private List<TagEntity> tags;
	
	// List<postDTO> 좀 나중에~
	// 얘는 아예 빼서 만들어야 html 갈아끼우기 가능!
}
