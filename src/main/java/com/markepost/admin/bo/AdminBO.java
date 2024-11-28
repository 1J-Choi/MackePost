package com.markepost.admin.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.markepost.admin.entity.AdminDto;
import com.markepost.admin.entity.AdminEntity;
import com.markepost.admin.entity.AdminId;
import com.markepost.admin.repository.AdminRepository;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBO {
	private final AdminRepository adminRepository;
	private final UserBO userBO;
	
	public void createAdmin(int boardId, int userId, boolean adminType) {
		AdminEntity admin = AdminEntity.builder()
				.adminId(new AdminId(boardId, userId))
				.adminType(adminType) 
				.createdAt(LocalDateTime.now())
				.build();
		adminRepository.save(admin);
	}
	
	public List<AdminDto> getAdminByBoardId(int boardId) {
		List<AdminDto> adminDtoList = new ArrayList<>();
		List<AdminEntity> adminList = adminRepository.findByAdminIdBoardId(boardId);
		
		for(AdminEntity admin : adminList) {
			AdminDto adminDto = new AdminDto();
			UserEntity user = userBO.getUserEntityById(admin.getAdminId().getUserId());
			adminDto.setUserId(admin.getAdminId().getUserId());
			adminDto.setName(user.getName());
			adminDto.setLoginId(user.getLoginId());
			adminDto.setAdminType(admin.isAdminType());
			adminDtoList.add(adminDto);
		}
		
		return adminDtoList;
	}
	
	public boolean existsByboardIdAndUserId(int boardId, Integer userId) {
		AdminId adminId = new AdminId();
		adminId.setBoardId(boardId);
		if(userId == null) {
			return false;
		}
		adminId.setUserId(userId);
		return adminRepository.existsByAdminId(adminId);
	}
	
	public void deleteAdminEntity(int boardId, int userId) {
		AdminId adminId = new AdminId(boardId, userId);
		
		AdminEntity admin = adminRepository.findById(adminId).orElse(null);
		
		adminRepository.delete(admin);
	}
	
	public AdminEntity getAdminEntityById(int boardId, int userId) {
		AdminId adminId = new AdminId(boardId, userId);
		return adminRepository.findById(adminId).orElse(null);
	}
}
