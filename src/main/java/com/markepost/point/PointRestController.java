package com.markepost.point;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.common.RandomService;
import com.markepost.pay.bo.PayBO;
import com.markepost.pay.entity.PayEntity;
import com.markepost.point.bo.PointBO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointRestController {
	private final PointBO pointBO;
	private final PayBO payBO;
	
	@PostMapping("/create-pay") 
	public Map<String, Object> createPay(
			@RequestParam("amount") int amount, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		
		// TODO: pay정보 저장하기
		PayEntity pay = payBO.addPay(amount, userId);
		
		Map<String, Object> result = new HashMap<>();
		if(pay != null) {
			result.put("code", 200);
			result.put("orderId", pay.getOrderId());
			result.put("payId", pay.getId());
			result.put("result", "성공");
		} else {
			result.put("code", 400);
			result.put("error_message", "결재 정보 저장에 실패했습니다.");
		}
		return result;
	}
}
