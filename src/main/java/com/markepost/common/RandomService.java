package com.markepost.common;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class RandomService {
	// 랜덤 문자열 만들기
	public String createRandCode(String data, int stringLength) {
		SecureRandom r = new SecureRandom();
		
		if (stringLength < 1) {
			throw new IllegalArgumentException("length must be a positive number.");
		}
			
	    StringBuilder sb = new StringBuilder(stringLength);
	    for (int i = 0; i < stringLength; i++) {
	        sb.append( data.charAt(
	            r.nextInt(data.length())
	        ));
	    }
	        
	    return sb.toString();
	}
}
