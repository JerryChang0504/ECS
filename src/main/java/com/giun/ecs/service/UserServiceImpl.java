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

  /**
   * 根據 username 查找使用者
   * 
   * @param username
   */
  @Override
  public UserInfo findUserByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  /**
   * 儲存使用者
   * 
   * @param userInfo
   */
  @Override
  public UserInfo save(UserInfo userInfo) {
    return userRepository.save(userInfo);
  }

  /**
   * 判斷帳號是否存在
   * 
   * @param username
   */
  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  /**
   * 判斷 Email 是否存在
   * 
   * @param email
   */
  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * 取得所有使用者
   */
  @Override
  public List<UserInfo> findAll() {
    return userRepository.findAll();
  }

}
