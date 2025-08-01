package com.giun.ecs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePswRequest {

  @NotBlank
  private String username;

  @NotBlank
  private String newPassword;

}
