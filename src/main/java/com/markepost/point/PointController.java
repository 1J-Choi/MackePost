package com.markepost.point;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
	private final UserBO userBO;
	
	@Value("${payment.toss.test_client_api_key}")
	private String clientKey;
	
	@GetMapping("/pay-point-view")
	public String payPointView(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		UserEntity user = userBO.getUserEntityById(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("clientKey", clientKey);
		
		return "point/payPoint";
	}
}
