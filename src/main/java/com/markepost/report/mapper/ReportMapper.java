package com.markepost.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.markepost.report.domain.Report;

@Mapper
public interface ReportMapper {
	public void insertReport(Report report);
	public int countByBoardId(int boardId);
	public List<Report> findByBoardId(
			@Param("boardId") int boardId, 
			@Param("pageSize") int pageSize, 
			@Param("offset") int offset);
}
