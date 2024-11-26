package com.markepost.tag.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.tag.entity.TagEntity;
import com.markepost.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagBO {
	private final TagRepository tagRepository;
	
	public List<TagEntity> getTagListByBoardId(int boardId) {
		return tagRepository.findByBoardId(boardId);
	}
}
