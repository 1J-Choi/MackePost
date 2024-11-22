package com.markepost.user.bo;

import org.springframework.stereotype.Service;

import com.markepost.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserBO {
	private final UserRepository userRepository;
	
	
}
