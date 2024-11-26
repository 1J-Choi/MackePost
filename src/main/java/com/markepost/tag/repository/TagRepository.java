package com.markepost.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.tag.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Integer>{
	public List<TagEntity> findByBoardId(int boardId);
}
