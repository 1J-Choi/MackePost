package com.markepost.tag.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostTagDTO {
	private int id;
	private String tagName;
	private String tagTradeType;
}
