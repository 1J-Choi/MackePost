package com.markepost.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markepost.admin.entity.AdminEntity;
import com.markepost.admin.entity.AdminId;

public interface AdminRepository extends JpaRepository<AdminEntity, AdminId>{

}
