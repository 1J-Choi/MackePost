package com.markepost.admin.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDto {
	private int userId;
	private String loginId;
	private String name;
	private boolean adminType;
}
