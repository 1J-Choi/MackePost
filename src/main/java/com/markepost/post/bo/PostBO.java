package com.markepost.post.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.admin.entity.AdminEntity;
import com.markepost.comment.bo.CommentBO;
import com.markepost.comment.domain.CommentDTO;
import com.markepost.common.FileManagerService;
import com.markepost.image.bo.ImageBO;
import com.markepost.image.entity.ImageEntity;
import com.markepost.like.bo.LikeBO;
import com.markepost.page.generic.Page;
import com.markepost.post.domain.MarketPost;
import com.markepost.post.domain.Post;
import com.markepost.post.domain.PostDetailDTO;
import com.markepost.post.domain.PostSearchDTO;
import com.markepost.post.domain.PostUpdateDTO;
import com.markepost.post.mapper.PostMapper;
import com.markepost.tag.bo.TagBO;
import com.markepost.tag.entity.TagEntity;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBO{
	private final FileManagerService fileManager;
	private final PostMapper postMapper;
	private final ImageBO imageBO;
	private final UserBO userBO;
	private final AdminBO adminBO;
	private final TagBO tagBO;
	private final LikeBO likeBO;
	private final CommentBO commentBO;
	
	public Post getPostById(int postId) {
		return postMapper.selectPostById(postId);
	}
	
	public MarketPost getMarketPost(int postId) {
		return postMapper.selectMarketPostById(postId);
	}
	
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
	
	public Page<PostSearchDTO> getSearchPost(
			int boardId, Integer tagId, int page,
			String subjectText, List<Integer> userIdList) {
		Page<PostSearchDTO> posts = new Page<>();
		posts.setNowPage(page);
		posts.setPageSize(10);
		posts.setTotalCount(postMapper.count(boardId, tagId, subjectText, userIdList));
		
		List<Post> postList = postMapper.selectPostSearchDTO
				(boardId, tagId, subjectText, userIdList, posts.getPageSize(), posts.getOffset());
		for(Post post : postList) {
			PostSearchDTO postSearchDTO = new PostSearchDTO();
			postSearchDTO.setPostId(post.getId());
			postSearchDTO.setTag(tagBO.getTagById(post.getTagId()));
			postSearchDTO.setSubject(post.getSubject());
			MarketPost marketPost = postMapper.selectMarketPostById(post.getId());
			if(marketPost != null) {
				postSearchDTO.setDone(marketPost.isDone()); // 미완료 => false, 완료 => true
			}
			UserEntity user = userBO.getUserEntityById(post.getUserId());
			postSearchDTO.setWriterId(user.getId());
			postSearchDTO.setWriterName(user.getName());
			postSearchDTO.setConfirmedUser(user.isConfirmed());
			AdminEntity userAdmin = adminBO.getAdminEntityById(boardId, user.getId());
			if(userAdmin == null) { // 어드민이 아닐 시
				postSearchDTO.setAdmin(false);
				postSearchDTO.setMain(false);
			} else { // 어드민일 시
				postSearchDTO.setAdmin(true);
				// adminType이 true => 메인 어드민이다 => true
				// adminType이 false => 서브 어드민이다 => false
				// boolean값이 똑같이 들어감으로 adminType을 그대로 넣어준다
				postSearchDTO.setMain(userAdmin.isAdminType());
			}
			postSearchDTO.setCreatedAt(post.getCreatedAt());
			// TODO: like 갯수 가져오기
			postSearchDTO.setLikeCount(likeBO.getLikeCount(post.getId())); // 임시
			
			posts.add(postSearchDTO);
		}
		
		return posts;
	}
	
	public PostDetailDTO getPostDetailDTOById(int postId, Integer userId) {
		PostDetailDTO postDetailDTO = new PostDetailDTO();
		Post post = postMapper.selectPostById(postId);
		UserEntity user = userBO.getUserEntityById(post.getUserId());
		List<ImageEntity> imageList = imageBO.getImageList(postId);
		TagEntity tag = tagBO.getTagById(post.getTagId());
		MarketPost marketPost = postMapper.selectMarketPostById(post.getId());
		List<CommentDTO> commentList = commentBO.getCommentDTOList(post);
		postDetailDTO.setPost(post);
		postDetailDTO.setUser(user);
		postDetailDTO.setImageList(imageList);
		postDetailDTO.setConfirmedUser(user.isConfirmed());
		AdminEntity userAdmin = adminBO.getAdminEntityById(post.getBoardId(), user.getId());
		if(userAdmin == null) { // 어드민이 아닐 시
			postDetailDTO.setAdmin(false);
			postDetailDTO.setMain(false);
		} else { // 어드민일 시
			postDetailDTO.setAdmin(true);
			// adminType이 true => 메인 어드민이다 => true
			// adminType이 false => 서브 어드민이다 => false
			// boolean값이 똑같이 들어감으로 adminType을 그대로 넣어준다
			postDetailDTO.setMain(userAdmin.isAdminType());
		}
		postDetailDTO.setTag(tag);
		postDetailDTO.setMarketPost(marketPost);
		postDetailDTO.setLikeCount(likeBO.getLikeCount(postId));
		postDetailDTO.setFilledLike(likeBO.getFilledLike(postId, userId));
		postDetailDTO.setCommentList(commentList);
		
		return postDetailDTO;
	}
	
	public int updateMarketPostIsDone(int postId, int userId) {
		return postMapper.updateMarketPostIsDone(postId, userId);
	}
	
	public PostUpdateDTO getPostUpdateDTO(int postId) {
		Post post = postMapper.selectPostById(postId);
		TagEntity tag = tagBO.getTagById(post.getTagId());
		MarketPost marketPost = postMapper.selectMarketPostById(postId);
		return new PostUpdateDTO(post, tag, marketPost);
	}
	
	public int updatePost(Post post, List<ImageEntity> images, List<MultipartFile> files, String userLoginId) {
		List<String> imagePathList = new ArrayList<>();
		if(files != null) {
			imagePathList = fileManager.uploadFile(files, userLoginId + "_post");
			if(imagePathList.get(0) != null && images.get(0).getImagePath() != null) {
				// 폴더, 이미지 제거(컴퓨터-서버)
				fileManager.deleteFile(images.get(0).getImagePath());
				imageBO.deleteImage(post.getId());
			}
			for (String imagePath : imagePathList) {
				imageBO.addImage(post.getId(), imagePath);
			}
		}
		
		return postMapper.updatePost(post);
	}
	
	public int updateMarketPost(MarketPost marketPost) {
		return postMapper.updateMarketPost(marketPost);
	}
}
