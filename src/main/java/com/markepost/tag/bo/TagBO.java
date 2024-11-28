package com.markepost.tag.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.tag.constant.TagType;
import com.markepost.tag.constant.TradeType;
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
	
	public TagEntity addTag(int boardId, String tagName, 
			String tagTypeString) {
		TagEntity tag = tagRepository.findByBoardIdAndTagName(boardId, tagName);
		if(tag != null) {
			return null;
		}
		TagEntity newTag;
		if(tagTypeString.equals("NORMAL")) { // 일반 태그일 경우
			newTag = TagEntity.builder()
					.boardId(boardId)
					.tagName(tagName)
					.tagType(TagType.valueOf(tagTypeString))
					.build();
		} else { // 인게임, 실물 태그일 경우 => /으로 분리 후 따로 저장
			newTag = TagEntity.builder()
					.boardId(boardId)
					.tagName(tagName)
					.tagType(TagType.valueOf(tagTypeString.split("/")[0]))
					.tradeType(TradeType.valueOf(tagTypeString.split("/")[1]))
					.build();
		}
		
		return tagRepository.save(newTag);
	}
}
