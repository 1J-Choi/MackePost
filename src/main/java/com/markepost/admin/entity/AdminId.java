package com.markepost.admin.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class AdminId implements Serializable {
	@Column(name = "boardId")
	private int boardId;
	
	@Column(name = "userId")
	private int userId;
}
