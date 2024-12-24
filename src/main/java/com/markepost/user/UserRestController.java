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
import com.markepost.point.bo.PointBO;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관련 API")
public class UserRestController {
	private final UserBO userBO;
	private final ConfirmBO confirmBO;
	private final PointBO pointBO;
	
	/**
	 * 아이디 중복 확인
	 * @param loginId
	 * @return
	 */
	@GetMapping("/is-duplicate-id")
	@Operation(summary = "로그인 아이디 중복 체크", 
	description = "입력된 로그인 아이디가 기존 사용자의 로그인 아이디와 중복되는지 체크합니다.")
	public Map<String, Object> isDuplicateId(
			@Parameter(description = "입력받은 로그인 아이디")
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
	@Operation(summary = "이메일 중복 체크", 
			description = "입력된 이메일이 기존 사용자의 이메일과 중복되는지 체크합니다.")
	@GetMapping("/is-duplicate-email")
	public Map<String, Object> isDuplicateEmail(
			@Parameter(description = "입력받은 이메일")
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
	@Operation(summary = "회원가입", 
			description = "로그인 아이디, 패스워드, 닉네임, 이메일을 받아 회원가입을 진행합니다.")
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@Parameter(description = "입력받은 로그인 아이디")
			@RequestParam("loginId") String loginId,
			@Parameter(description = "입력받은 비밀번호")
			@RequestParam("password") String password,
			@Parameter(description = "입력받은 닉네임")
			@RequestParam("name") String name,
			@Parameter(description = "입력받은 이메일")
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
	@Operation(summary = "로그인", 
			description = "로그인 아이디, 비밀번호를 입력받아 로그인합니다.")
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@Parameter(description = "입력받은 로그인 아이디")
			@RequestParam("loginId") String loginId,
			@Parameter(description = "입력받은 비밀번호")
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
	@Operation(summary = "유저 정보 수정", 
			description = "닉네임, 자기소개, 프로필 이미지를 받아 유저의 정보를 수정합니다.")
	@PatchMapping("/update")
	public Map<String, Object> userUpdate(
			@Parameter(description = "입력받은 닉네임")
			@RequestParam("name") String name,
			@Parameter(description = "입력받은 자기소개")
			@RequestParam("introduce") String introduce,
			@Parameter(description = "입력받은 이미지", required = false)
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
	@Operation(summary = "인증코드 발급", 
			description = "본인인증을 위한 인증코드를 발급하고, 이메일로 발송합니다.")
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
	
	@Operation(summary = "인증코드 확인", 
			description = "입력받은 인증코드가 일치하는지 확인한 뒤 본인인증을 완료합니다.")
	@PostMapping("/confirm/validate")
	public Map<String, Object> validateConfirm(
			@Parameter(description = "입력받은 인증코드")
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
			pointBO.createPoint(userId);
			result.put("code", 200);
			result.put("result", "성공");
		}
		
		return result;
	}
}
