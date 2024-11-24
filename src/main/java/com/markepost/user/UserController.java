package com.markepost.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserBO userBO;
	
	@GetMapping("/sign-in-view")
	public String signInView() {
		return "user/signIn";
	}
	
	@GetMapping("/sign-up-view")
	public String signUpView() {
		return "user/signUp";
	}
	
	@GetMapping("/sign-out")
	public String signOut(HttpSession session) {
		session.removeAttribute("userId");
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		session.removeAttribute("userImage");
		
		// 메인페이지로 이동
		return "redirect:/";
	}
	
	@GetMapping("/mypage-view")
	public String mypage(HttpSession session, Model model) {
		int userId = (int) session.getAttribute("userId");
		
		UserEntity user = userBO.getUserEntityById(userId);
		
		if(user == null) {
			return "/user/sign-in-view";
		}
		
		model.addAttribute("user", user);
		return "/user/userPage";
	}
	
	@GetMapping("/user-update-view")
	public String userUpdate(HttpSession session, Model model) {
		int userId = (int) session.getAttribute("userId");
		
		UserEntity user = userBO.getUserEntityById(userId);
		
		if(user == null) {
			return "/user/sign-in-view";
		}
		
		model.addAttribute("user", user);
		return "/user/userUpdate";
	}
}
