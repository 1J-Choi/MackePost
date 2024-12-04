package com.markepost.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.image.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{
	public List<ImageEntity> findAllByPostId(int postId);
}
