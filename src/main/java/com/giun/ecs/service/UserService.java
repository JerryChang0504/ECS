package com.giun.ecs.service;

import java.util.List;
import com.giun.ecs.entity.UserInfo;

/**
 * 使用者服務
 */
public interface UserService {

  /** 根據 username 查找使用者 */
  public UserInfo findUserByUsername(String username);

  /**
   * 儲存使用者
   * 
   * @param userInfo
   */
  public UserInfo save(UserInfo userInfo);

  /**
   * 判斷帳號是否存在
   * 
   * @param username
   */
  public boolean existsByUsername(String username);

  /**
   * 判斷 Email 是否存在
   * 
   * @param email
   * @return
   */
  public boolean existsByEmail(String email);

  /**
   * 取得所有使用者
   * 
   * @return
   */
  public List<UserInfo> findAll();
}
