package com.markepost.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.markepost.comment.bo.CommentBO;
import com.markepost.comment.domain.CommentTopDTO;
import com.markepost.post.bo.PostBO;
import com.markepost.post.domain.PostTopDTO;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserBO userBO;
	private final PostBO postBO;
	private final CommentBO commentBO;
	
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
			return "user/sign-in-view";
		}
		
		// 최근 게시글, 댓글 5개씩 출력
		List<PostTopDTO> top5PostList = postBO.getTop5PostList(userId);
		List<CommentTopDTO> top5CommentList = commentBO.getTop5CommentList(userId);
				
		model.addAttribute("top5PostList", top5PostList);
		model.addAttribute("top5CommentList", top5CommentList);
		model.addAttribute("user", user);
		return "user/userPage";
	}
	
	@GetMapping("/userpage-view")
	public String userPage(@RequestParam("userId") int userId, Model model) {
		UserEntity user = userBO.getUserEntityById(userId);
		
		// 최근 게시글/댓글 5개씩 출력
		List<PostTopDTO> top5PostList = postBO.getTop5PostList(userId);
		List<CommentTopDTO> top5CommentList = commentBO.getTop5CommentList(userId);
				
		model.addAttribute("top5PostList", top5PostList);
		model.addAttribute("top5CommentList", top5CommentList);
		model.addAttribute("user", user);
		return "user/userPage";
	}
	
	@GetMapping("/user-update-view")
	public String userUpdate(HttpSession session, Model model) {
		int userId = (int) session.getAttribute("userId");
		
		UserEntity user = userBO.getUserEntityById(userId);
		
		if(user == null) {
			return "user/sign-in-view";
		}
		
		model.addAttribute("user", user);
		return "user/userUpdate";
	}
	
	@GetMapping("/confirm-view")
	public String confirmView(HttpSession session, Model model, 
			RedirectAttributes redirectAttributes) {
		int userId = (int) session.getAttribute("userId");
		
		// 이미 인증된 유저의 접근 차단
		UserEntity user = userBO.getUserEntityById(userId);
		if(user.isConfirmed()) {
			redirectAttributes.addFlashAttribute("suspendMessage", "이미 인증이 완료된 유저입니다.");
			return "redirect:/suspend/suspendView";
		}
		
		String email = userBO.getUserEntityById(userId).getEmail();
		
		model.addAttribute("email", email);
		return "user/confirmView";
	}
}
