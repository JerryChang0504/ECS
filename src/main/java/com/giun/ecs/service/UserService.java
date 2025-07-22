package com.giun.ecs.service;

import java.util.List;

import com.giun.ecs.entity.UserInfo;

public interface UserService {

  public UserInfo findUserByUsername(String username);

  public UserInfo save(UserInfo userInfo);

  public boolean existsByUsername(String username);

  public boolean existsByEmail(String email);

  public List<UserInfo> findAll();
}
