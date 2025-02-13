package com.markepost.point;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.markepost.common.RandomService;
import com.markepost.page.generic.Page;
import com.markepost.pay.bo.PayBO;
import com.markepost.pay.constant.PayStatus;
import com.markepost.pay.entity.PayEntity;
import com.markepost.point.bo.PointBO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
@Tag(name = "Point API", description = "포인트 관련 API")
public class PointRestController {
	private final PointBO pointBO;
	private final PayBO payBO;
	
	@PostMapping("/create-pay") 
	@Operation(summary = "결재 정보 저장", 
	description = "tossPayments에서 사용될 결재 금액 정보를 받고, orderId를 생성한 뒤 저장 및 반환합니다.")
	public Map<String, Object> createPay(
			@Parameter(description = "결재 금액")
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
	
	@GetMapping("/pay-list-view")
	@Operation(summary = "결재 내역 출력", 
	description = "결재 내역 리스트의 태그를 String으로 반환하며 page 값에 따라 5개씩 출력합니다.")
	public String payList(
			@Parameter(description = "현재 페이지")
			@RequestParam(value = "page", defaultValue = "1") int page, 
			HttpSession session) {
		int userId = (int) session.getAttribute("userId");
		Page<PayEntity> payPage = payBO.getPayPageByUserId(userId, page);
		
		String response = "<table class=\"table text-center\">"
				+ "<thead>"
				+ "<tr>"
				+ "<th>금액</th>"
				+ "<th>결재상태</th>"
				+ "<th>생성일</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>"
				+ "<tr>";
		// payList 세팅
		for(PayEntity pay : payPage.getItems()) {
			response = response + "<td>" + pay.getAmount() + "</td>"
					+ "<td><span class='" + pay.getPayStatus().getTextColor() + "'>" + pay.getPayStatus().getStatusText() + "</span></td>"
					+ "<td>" + pay.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</td>"
					+ "</tr>";
		}
		response = response + "</tbody></table>";
		// 페이지 기능 세팅
		response = response + "<div class='d-flex justify-content-center my-2'>";
		if(payPage.getNowPage() > 1) {
			response = response + "<a id='prevBtn' class='text-primary drag-prevent'>Previous </a>";
		}
		response = response + "<span class='mx-3'>Page " + payPage.getNowPage() 
			+ " of " + payPage.getTotalPages() + "</span>";
		if(payPage.getNowPage() < payPage.getTotalPages()) {
			response = response + "<a id='nextBtn' class='text-primary drag-prevent'> Next</a>";
		}
		response = response + "</div>";
		
		return response;
	}
}
