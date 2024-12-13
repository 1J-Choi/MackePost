package com.markepost.point;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.markepost.pay.bo.PayBO;
import com.markepost.point.bo.PointBO;
import com.markepost.user.bo.UserBO;
import com.markepost.user.entity.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
	private final UserBO userBO;
	private final PayBO payBO;
	
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
	
	@GetMapping("/create/success")
	public String paySuccess(
			@RequestParam("orderId") String orderId, 
			@RequestParam("paymentKey") String paymentKey, 
			@RequestParam("amount") int amount, 
			@RequestParam("payId") int payId, 
			HttpSession session, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		int userId = (int) session.getAttribute("userId");
		
		int point = payBO.paySuccess(orderId, payId, amount, userId);
		
		if(point == -1) { // 잘못된 충전 요청일 경우
			redirectAttributes.addFlashAttribute("suspendMessage", "잘못된 포인트 충전 요청입니다.");
			return "redirect:/suspend/suspendView";
		}
		
		model.addAttribute("point", point);
		
		return "point/pointResult";
	}
	
	@GetMapping("/create/fail")
	public String payFail(
			@RequestParam("code") String code, 
			@RequestParam("message") String message, 
			@RequestParam("orderId") String orderId, 
			@RequestParam("payId") int payId, 
			RedirectAttributes redirectAttributes) {
		
		payBO.payFail(payId);
		
		redirectAttributes.addFlashAttribute("suspendMessage", message);
		return "redirect:/suspend/suspendView";
	}
}
