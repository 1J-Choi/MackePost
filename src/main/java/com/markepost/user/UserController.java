package com.markepost.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@GetMapping("/sign-in-view")
	public String signInView() {
		return "user/signIn";
	}
	
	@GetMapping("/sign-up-view")
	public String signUpView() {
		return "user/signUp";
	}
	
//	@GetMapping("/sign-out")
//	public String signOut(HttpSession session) {
//		session.removeAttribute("userId");
//		session.removeAttribute("userLoginId");
//		session.removeAttribute("userName");
//		
//		// 로그인 페이지로 이동
//		return "redirect:/user/sign-in-view";
//	}
}
