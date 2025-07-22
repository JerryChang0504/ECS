package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserInfo findUserByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public UserInfo save(UserInfo userInfo) {
		return userRepository.save(userInfo);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public List<UserInfo> findAll() {
		return userRepository.findAll();
	}

}
