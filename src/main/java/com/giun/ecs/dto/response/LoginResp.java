package com.giun.ecs.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResp {

  private String userRole;
  private String token;

}
