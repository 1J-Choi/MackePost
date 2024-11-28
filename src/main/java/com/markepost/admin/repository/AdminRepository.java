package com.markepost.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.admin.entity.AdminEntity;
import com.markepost.admin.entity.AdminId;

public interface AdminRepository extends JpaRepository<AdminEntity, AdminId>{
	public List<AdminEntity> findByAdminIdBoardId(int boardId);
	public boolean existsByAdminId(AdminId AdminId);
}
