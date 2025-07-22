package com.giun.ecs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giun.ecs.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
	// 根據 username 查找使用者（用於登入）
	Optional<UserInfo> findByUsername(String username);

	// 確認 username 是否已存在
	boolean existsByUsername(String username);

	// 確認 email 是否已存在
	boolean existsByEmail(String email);

	// 根據 email 查找使用者（用於找回密碼等功能）
	Optional<UserInfo> findByEmail(String email);

	// 根據 username 與 status 查找啟用中的使用者（範例進階）
	Optional<UserInfo> findByUsernameAndStatus(String username, Integer status);
}
