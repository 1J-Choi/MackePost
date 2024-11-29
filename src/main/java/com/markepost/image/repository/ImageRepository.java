package com.markepost.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.image.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{

}
