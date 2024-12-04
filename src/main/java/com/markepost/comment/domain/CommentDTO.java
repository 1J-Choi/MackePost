package com.markepost.comment.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
	private Comment comment;
	private String userName;
	private boolean isComfirmed;
	private boolean isAdmin;
	private boolean isMain;
	private List<SubCommentDTO> subCommentList;
}
