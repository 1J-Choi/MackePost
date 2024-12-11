package com.markepost.user;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.markepost.confirm.bo.ConfirmBO;
import com.markepost.confirm.entity.ConfirmEntity;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
	private final UserBO userBO;
	private final ConfirmBO confirmBO;
	
	/**
	 * 아이디 중복 확인
	 * @param loginId
	 * @return
	 */
	@GetMapping("/is-duplicate-id")
	public Map<String, Object> isDuplicateId(
			@RequestParam("loginId") String loginId) {
		// DB select
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		boolean isDuplicateId = false;
		if(user != null) {
			isDuplicateId = true;
		}
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("is_duplicate_id", isDuplicateId);
		return result;
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/is-duplicate-email")
	public Map<String, Object> isDuplicateEmail(
			@RequestParam("email") String email) {
		// DB select
		UserEntity user = userBO.getUserEntityByEmail(email);
		boolean isDuplicateEmail = false;
		if(user != null) {
			isDuplicateEmail = true;
		}
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("is_duplicate_email", isDuplicateEmail);
		return result;
	}
	
	/**
	 * 회원가입
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		// BCrypt 알고리즘 으로 password 해싱
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		// db insert
		UserEntity user = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패하였습니다.");
		}
		return result;
	}
	
	/**
	 * 로그인
	 * @param loginId
	 * @param password
	 * @param request
	 * @return
	 */
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		// db select
		UserEntity user = userBO.getUser(loginId, password);
		
		// return
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			// 세션에 사용자 정보를 담는다.(사용자 각각)
			HttpSession session = request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			session.setAttribute("userImage", user.getImagePath());
			result.put("code", 200);
			result.put("result", "성공");	
		} else {
			result.put("code", 300);
			result.put("error_message", "존재하지 않는 사용자 입니다.");
		}
		return result;
	}
	
	
	/**
	 * 유저 정보 수정
	 * @param name
	 * @param introduce
	 * @param file
	 * @param session
	 * @return
	 */
	@PatchMapping("/update")
	public Map<String, Object> userUpdate(
			@RequestParam("name") String name,
			@RequestParam("introduce") String introduce,
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		String userLoginId = (String) session.getAttribute("userLoginId");
		
		// db update
		UserEntity user = userBO.updateUser(userId, userLoginId, name, introduce, file);
		
		// return result
		Map<String, Object> result = new HashMap<>();
		if(user != null) {
			result.put("code", 200);
			result.put("result", "성공");
			session.setAttribute("userName", user.getName());
			session.setAttribute("userImage", user.getImagePath());
		} else {
			result.put("code", 500);
			result.put("error_message", "정보가 수정되지 않았습니다.");
		}
		return result;
	}
	
	/**
	 * 인증번호 발급
	 * @param session
	 * @return
	 */
	@PostMapping("/confirm/create")
	public Map<String, Object> createConfirm(HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		ConfirmEntity confirm = confirmBO.addConfirm(userId);
		Map<String, Object> result = new HashMap<>();
		if(confirm != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "인증코드 발급 중 문제가 발생했습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/confirm/validate")
	public Map<String, Object> validateConfirm(
			@RequestParam("confirmCode") String confirmCode, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		ConfirmEntity confirm = confirmBO.getConfirm(userId);
		Map<String, Object> result = new HashMap<>();
		if(confirm == null) { // 인증코드 발급 안됨
			result.put("code", 400);
			result.put("error_message", "no_confirmCode_exist");
		} else if (!confirm.getConfirmCode().equals(confirmCode)) {
			// 인증코드 불일치
			result.put("code", 400);
			result.put("error_message", "worng_confirmCode");
		} else if (confirm.getConfirmCode().equals(confirmCode)) {
			// 인증코드 일치 => 성공
			userBO.updateUserConfirm(userId);
			result.put("code", 200);
			result.put("result", "성공");
		}
		
		return result;
	}
}
