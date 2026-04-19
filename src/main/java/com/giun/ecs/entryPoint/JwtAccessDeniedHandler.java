package com.giun.ecs.entryPoint;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.enums.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 處理已完成認證但權限不足的請求。
 * 當 JWT 驗證成功，但使用者角色不符合 API 的授權條件時，
 * 統一由這裡回傳 403。
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * 回傳標準的 403 JSON 結構，讓前端可明確判斷為權限不足。
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    Outbound error = Outbound.error(ResultCode.USER_IS_NOT_AUTHORIZED);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(response.getWriter(), error);
  }

}
