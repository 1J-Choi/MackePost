package com.markepost.post.domain;

import com.markepost.tag.entity.TagEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUpdateDTO {
	private Post post;
	private TagEntity tag;
	private MarketPost marketPost;
	// 이미지는 일단 다시 등록하면 기존것은 싹 미는걸로
}
