package com.markepost.user.bo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserBOTest {

	@Autowired
	UserBO userBO;
	
	@Transactional // rollback
	@Test
	void 회원가입() {
		userBO.addUser("테스트1", "암호화된비번", "테스트", "test@test.com");
		
	}

}
