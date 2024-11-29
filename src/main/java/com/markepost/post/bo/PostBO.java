package com.markepost.post.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.common.FileManagerService;
import com.markepost.image.bo.ImageBO;
import com.markepost.post.domain.Post;
import com.markepost.post.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBO {
	private final FileManagerService fileManager;
	private final PostMapper postMapper;
	private final ImageBO imageBO;
	
	public Post addNormalPost(
			int boardId, int tagId, String subject, 
			String content, List<MultipartFile> files, 
			int userId, String userLoginId) {
		Post post = new Post();
		post.setBoardId(boardId);
		if(tagId != 0) {
			post.setTagId(tagId);
		}
		post.setSubject(subject);
		post.setContent(content);
		post.setUserId(userId);
		postMapper.insertNormalPost(post);
		
		List<String> imagePathList = new ArrayList<>();
		if(files != null) {
			imagePathList = fileManager.uploadFile(files, userLoginId + "_post");
			for (String imagePath : imagePathList) {
				imageBO.addImage(post.getId(), imagePath);
			}
		}
		
		return post;
	}
	
	public Post addMarketPost(
			int boardId, int tagId, String subject, 
			String itemName, Integer price, String content, 
			List<MultipartFile> files, int userId, String userLoginId) {
		Post post = new Post();
		post.setBoardId(boardId);
		if(tagId != 0) {
			post.setTagId(tagId);
		}
		post.setSubject(subject);
		post.setContent(content);
		post.setUserId(userId);
		postMapper.insertNormalPost(post);
		// 거래글 marketPost 저장하기
		postMapper.insertMarketPost(post.getId(), itemName, price, false);
		
		List<String> imagePathList = new ArrayList<>();
		if(files != null) {
			imagePathList = fileManager.uploadFile(files, userLoginId + "_post");
			for (String imagePath : imagePathList) {
				imageBO.addImage(post.getId(), imagePath);
			}
		}
		
		return post;
	}
}
