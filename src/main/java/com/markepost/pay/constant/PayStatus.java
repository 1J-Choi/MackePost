package com.markepost.pay.constant;

public enum PayStatus {
	REQUEST("text-secondary", "요청중"), 
	SUCCESS("text-success", "성공"), 
	FAIL("text-danger", "실패");
	
	private final String textColor;
	private final String statusText;
	
	// 생성자
    PayStatus(String textColor, String statusText) {
        this.textColor = textColor;
        this.statusText = statusText;
    }

    // 색깔 텍스트를 반환하는 메서드
    public String getTextColor() {
        return this.textColor;
    }
    
    // 한글 텍스트를 반환하는 메소드
    public String getStatusText() {
    	return this.statusText;
    }
}
