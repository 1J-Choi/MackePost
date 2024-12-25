package com.markepost.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.admin.bo.AdminBO;
import com.markepost.admin.entity.AdminEntity;
import com.markepost.board.bo.BoardBO;
import com.markepost.comment.bo.CommentBO;
import com.markepost.post.bo.PostBO;
import com.markepost.report.bo.ReportBO;
import com.markepost.report.constant.ReportType;
import com.markepost.suspend.bo.SuspendBO;
import com.markepost.suspend.constant.SuspendType;
import com.markepost.suspend.entity.SuspendEntity;
import com.markepost.tag.bo.TagBO;
import com.markepost.tag.entity.TagEntity;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "게시판 관리자 기능 관련 API")
public class AdminRestController {
	private final BoardBO boardBO;
	private final TagBO tagBO;
	private final UserBO userBO;
	private final AdminBO adminBO;
	private final SuspendBO suspendBO;
	private final PostBO postBO;
	private final CommentBO commentBO;
	private final ReportBO reportBO;

	/**
	 * 게시판 수정
	 * @param boardId
	 * @param introduce
	 * @param file
	 * @param session
	 * @return
	 */
	@Operation(summary = "게시판 수정", 
			description = "게시판의 소개, 이미지를 수정합니다.")
	@PatchMapping("/board/update")
	public Map<String, Object> boardUpdate(
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId,
			@Parameter(description = "게시판 소개")
			@RequestParam("introduce") String introduce,
			@Parameter(description = "게시판 아이콘 이미지", required = false)
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		// userId 받아오기
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		int rowCount = boardBO.updateBoard(boardId, introduce, file, userId, userLoginId);
		Map<String, Object> result = new HashMap<>();
		if(rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "게시판 정보를 수정하는데 문제가 발생하였습니다.");
		}
		return result;
	}
	
	/**
	 * 태그 생성
	 * @param boardId
	 * @param tagName
	 * @param tagTypeString
	 * @return
	 */
	@Operation(summary = "태그 생성", 
			description = "관리자가 게시판 내에서 쓸 수 있는 태그를 생성합니다.")
	@PostMapping("/board/create-tag")
	public Map<String, Object> createTag(
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId,
			@Parameter(description = "태그 이름")
			@RequestParam("tagName") String tagName,
			@Parameter(description = "태그의 종류(일반과 거래로 나뉘며 거래의 경우 인게임과 실물, 여기서 또 구매, 판매, 교환, 기타로 나누어진다)")
			@RequestParam("tagTypeString") String tagTypeString) {
		TagEntity tag = tagBO.addTag(boardId, tagName, tagTypeString);
		
		Map<String, Object> result = new HashMap<>();
		if(tag != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "이미 존재하는 이름의 태그입니다.");
		}
		
		return result;
	}
	
	/**
	 * 어드민 ID 조회
	 * @param adminLoginId
	 * @param boardId
	 * @return
	 */
	@Operation(summary = "유저 로그인 아이디 조회", 
			description = "서브 관리자를 등록하기 전 로그인 아이디를 입력하여 해당 유저가 존재하고 이미 해당 게시판의 어드민이 아닌지 확인합니다.")
	@GetMapping("/user/search-by-loginid")
	public Map<String, Object> searchByLoginId(
			@Parameter(description = "검색하고자 하는 유저의 로그인 아이디")
			@RequestParam("adminLoginId") String adminLoginId, 
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId) {
		Map<String, Object> result = new HashMap<>();
		
		// 유저 존재 유무
		UserEntity exUser = userBO.getUserEntityByLoginId(adminLoginId);
		// 해당 아이디를 가진 유저가 없을 경우
		if(exUser == null) {
			result.put("code", 200);
			result.put("is_existed_loginId", false);
			return result;
		}
		
		// 게시판에 중복된 admin 존재 유무 => 중복된다 true, 중복 안됨 false
		boolean hasAdmin = adminBO.existsByboardIdAndUserId(boardId, exUser.getId());
		result.put("code", 200);
		result.put("is_existed_loginId", true);
		result.put("is_duplicated_loginId", hasAdmin);
		return result;
	}
	
	/**
	 * subAmdin 등록
	 * @param adminLoginId
	 * @param boardId
	 * @return
	 */
	@Operation(summary = "서브 관리자 등록", 
			description = "조회를 마친 유저를 서브 어드민으로 등록합니다.")
	@PostMapping("/board/create-subadmin")
	public Map<String, Object> addSubAdmin(
			@Parameter(description = "서브 관리자로 등록할 유저의 로그인 아이디")
			@RequestParam("adminLoginId") String adminLoginId, 
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId,
			HttpSession session) {
		// mainAdmin이 삭제하는 것인지 체크하기 위해 session의 userId 추출
		int nowUserId = (int) session.getAttribute("userId");
		AdminEntity nowAdmin = adminBO.getAdminEntityById(boardId, nowUserId);
		
		Map<String, Object> result = new HashMap<>();
		if(!nowAdmin.isAdminType()) {
			result.put("code", 403);
			result.put("error_message", "관리자 등록은 메인 관리지만 가능합니다.");
			return result;
		}
		
		UserEntity user = userBO.getUserEntityByLoginId(adminLoginId);
		adminBO.createAdmin(boardId, user.getId(), false);
		
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	@Operation(summary = "서브 관리자 삭제", 
			description = "등록된 서브 관리자 권한을 삭제합니다. (메인 관리자만 가능)")
	@DeleteMapping("/board/delete-subadmin")
	public Map<String, Object> deleteSubAdmin(
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId, 
			@Parameter(description = "권한 삭제할 서브 관리자의 ID")
			@RequestParam("userId") int userId, 
			HttpSession session) {
		// mainAdmin이 삭제하는 것인지 체크하기 위해 session의 userId 추출
		int nowUserId = (int) session.getAttribute("userId");
		AdminEntity nowAdmin = adminBO.getAdminEntityById(boardId, nowUserId);
		
		Map<String, Object> result = new HashMap<>();
		if(!nowAdmin.isAdminType()) {
			result.put("code", 403);
			result.put("error_message", "관리자 삭제는 메인 관리지만 가능합니다.");
			return result;
		}
		
		adminBO.deleteAdminEntity(boardId, userId);
		
		result.put("code", 200);
		result.put("result", "성공");
		return result;
	}
	
	@Operation(summary = "유저 제재 생성", 
			description = "게시판 관리자가 신고받은 유저 또는 허위 신고를 한 유저에 대해 원하는 종류와 기간의 제재를 적용합니다.")
	@PostMapping("/suspend/create")
	public Map<String, Object> createSuspend(
			@Parameter(description = "제재를 받을 유저의 ID")
			@RequestParam("userId") int userId, 
			@Parameter(description = "게시판 ID")
			@RequestParam("boardId") int boardId,
			@Parameter(description = "제재 종류 (게시판 접속 금지, 게시글 작성 금지, 댓글 작성 금지)")
			@RequestParam("suspendType") SuspendType suspendType, 
			@Parameter(description = "제재기간의 양")
			@RequestParam("number") int number, 
			@Parameter(description = "제재 기간의 단위 (시, 일, 주, 월, 년)")
			@RequestParam("time") String time, 
			HttpSession session) {
		// 현재 접속한 사람이 제재를 주는 admin => 접속자의 id가 admin의 id
		int adminId = (int) session.getAttribute("userId");
		
		SuspendEntity suspendEntity = suspendBO.addSuspend(userId, boardId, suspendType, number, time, adminId);
		Map<String, Object> result = new HashMap<>();
		if(suspendEntity != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("result", "제재 적용 중 문제가 발생했습니다.");
		}
		return result;
	}
	
	@Operation(summary = "신고받은 컨텐츠 삭제", 
			description = "신고받은 컨텐츠(게시글, (대)댓글)를 삭제처리 합니다.")
	@PatchMapping("/report/delete-post-comment")
	public Map<String, Object> deletePostComment(
			@Parameter(description = "삭제할 컨텐츠의 종류 (게시글, (대)댓글)")
			@RequestParam("reportType") ReportType reportType, 
			@Parameter(description = "삭제할 컨텐츠의 ID")
			@RequestParam("fkId") int fkId, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		if(reportType.equals(ReportType.POST)) { // 게시글
			postBO.updatePostisDeleted(fkId);
		} else {
			commentBO.updateCommentDeleted(reportType.name(), fkId);
		}
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		
		return result;
	}
	
	@Operation(summary = "신고 삭제", 
			description = "관리자가 신고를 삭제합니다.")
	@DeleteMapping("/report/delete")
	public Map<String, Object> deleteReport(
			@Parameter(description = "삭제할 신고의 ID")
			@RequestParam("reportId") int reportId, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		int rowCount = reportBO.deleteReport(reportId);
		Map<String, Object> result = new HashMap<>();
		if(rowCount > 0) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("result", "신고 삭제에 실패했습니다.");
		}
		return result;
	}
}
