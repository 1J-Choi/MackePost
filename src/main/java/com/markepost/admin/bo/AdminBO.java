package com.markepost.admin.bo;

import org.springframework.stereotype.Service;

import com.markepost.admin.entity.AdminEntity;
import com.markepost.admin.entity.AdminId;
import com.markepost.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBO {
	private final AdminRepository adminRepository;
	
	public void createAdmin(int boardId, int userId, boolean adminType) {
		AdminEntity admin = AdminEntity.builder()
				.adminId(new AdminId(boardId, userId))
				.adminType(adminType)
				.build();
		adminRepository.save(admin);
	}
}
