package com.markepost.tag.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.tag.constant.TagType;
import com.markepost.tag.constant.TradeType;
import com.markepost.tag.domain.PostTagDTO;
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
	
	public TagEntity getTagById(Integer tagId) {
		if(tagId == null) {
			return null;
		}
		return tagRepository.findById(tagId).orElse(null);
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
	
	public List<PostTagDTO> getpostTagDTOListByBoardId(int boardId) {
		List<TagEntity> tagList = tagRepository.findByBoardId(boardId);
		List<PostTagDTO> postTagDTOList = new ArrayList<>();
		
		for (TagEntity tag : tagList) {
			String tagTradeType = "";
			if(tag.getTagType() == TagType.NORMAL) {
				tagTradeType = tag.getTagType().toString();
			} else {
				tagTradeType = tag.getTagType().toString() + "/" + tag.getTradeType().toString();
			}
			
			PostTagDTO postTagDTO = new PostTagDTO(tag.getId(), tag.getTagName(), tagTradeType);
			postTagDTOList.add(postTagDTO);
		}
		
		return postTagDTOList;
	}
}
