package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.ChangePswReq;
import com.giun.ecs.dto.request.LoginReq;
import com.giun.ecs.dto.request.RegisterReq;
import com.giun.ecs.dto.request.UpdateUserReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.UserResponse;
import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.enums.ResultCode;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.utils.JwtUtil;

@Service
public class AuthService {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 使用者註冊
	 * 
	 * @param req
	 * @return
	 */
	public Outbound register(RegisterReq req) throws ApplicationException {
		// 帳號與 Email 重複檢查
		if (userService.existsByUsername(req.getUsername())) {
			throw new ApplicationException(ResultCode.ACCOUNT_IS_EXIST);
		}

		if (userService.existsByEmail(req.getEmail())) {
			throw new ApplicationException(ResultCode.EMAIL_IS_EXIST);
		}

		UserInfo userInfo = UserInfo.builder().username(req.getUsername())
				.password(passwordEncoder.encode(req.getPassword()))
				.email(req.getEmail()).fullName(req.getFullName())
				.phone(req.getPhone()).build();

		userService.save(userInfo);
		return Outbound.ok("註冊成功");

	}

	/**
	 * 使用者登入
	 * 
	 * @param req
	 * @return
	 */
	public Outbound login(LoginReq req) throws ApplicationException {
		UserInfo userInfo = userService.findUserByUsername(req.getUsername());

		if (userInfo == null) {
			throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
		}

		if (!passwordEncoder.matches(req.getPassword(),
				userInfo.getPassword())) {
			throw new ApplicationException(ResultCode.PASSWORD_NOT_MATCH);
		}
		String token = jwtUtil.generateToken(userInfo);

		return Outbound.ok(token);
	}

	/**
	 * 取得使用者資料
	 * 
	 * @param token
	 * @return
	 */
	public Outbound findUserByUsername(String token)
			throws ApplicationException {

		String username = jwtUtil.getUsernameFromToken(token);
		UserInfo userInfo = userService.findUserByUsername(username);

		if (userInfo == null) {
			throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
		}

		UserResponse userResponse = UserResponse.builder().id(userInfo.getId())
				.username(userInfo.getUsername()).email(userInfo.getEmail())
				.fullName(userInfo.getFullName()).phone(userInfo.getPhone())
				.role(userInfo.getRole()).createdAt(userInfo.getCreatedAt())
				.build();

		return Outbound.ok(userResponse);
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param username
	 * @param request
	 * @return
	 */
	public Outbound updateUserProfile(String username,
			UpdateUserReq request) throws Exception {
		UserInfo userInfo = userService.findUserByUsername(username);

		if (userInfo == null) {
			throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
		}

		userInfo.setFullName(request.getFullName());
		userInfo.setPhone(request.getPhone());
		userInfo.setEmail(request.getEmail());

		try {
			UserInfo upUserInfo = userService.save(userInfo);
			return Outbound.ok(upUserInfo);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * 取得所有使用者資料
	 * 
	 * @return
	 */

	public Outbound getCurrentUser() {
		List<UserInfo> users = userService.findAll();
		List<UserResponse> userResponses = users.stream()
				.map(userInfo -> UserResponse.builder().id(userInfo.getId())
						.username(userInfo.getUsername())
						.email(userInfo.getEmail())
						.fullName(userInfo.getFullName())
						.phone(userInfo.getPhone()).role(userInfo.getRole())
						.createdAt(userInfo.getCreatedAt()).build())
				.toList();
		return Outbound.ok(userResponses);
	}

	/**
	 * 更新密碼
	 * 
	 * @param request
	 * @return
	 */
	public Outbound updatePassword(ChangePswReq request) {
		UserInfo userInfo = userService
				.findUserByUsername(request.getUsername());
		userInfo.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userService.save(userInfo);
		return Outbound.ok("密碼更新成功");
	}

	/**
	 * 登出使用者
	 * 
	 * @return
	 */
	public Outbound logout() {
		return Outbound.ok("登出成功");
	}

}
