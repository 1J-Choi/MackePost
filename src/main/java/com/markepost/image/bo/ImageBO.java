package com.markepost.image.bo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.image.entity.ImageEntity;
import com.markepost.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageBO {
	private final ImageRepository imageRepository;
	
	public void addImage(int postId, String imagePath){
		ImageEntity image = ImageEntity.builder()
				.postId(postId)
				.imagePath(imagePath)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		imageRepository.save(image);
	}
	
	public List<ImageEntity> getImageList(int postId) {
		return imageRepository.findAllByPostId(postId);
	}
	
	public void deleteImage(int postId) {
		imageRepository.deleteByPostId(postId);
	}
}
