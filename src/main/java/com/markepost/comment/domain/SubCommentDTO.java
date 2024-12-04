package com.markepost.comment.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCommentDTO {
	private SubComment subComment;
	private String userName;
	private boolean isComfirmed;
	private boolean isAdmin;
	private boolean isMain;
}
