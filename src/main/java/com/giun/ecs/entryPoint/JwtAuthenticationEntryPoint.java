package com.giun.ecs.entryPoint;

import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.enums.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 處理尚未通過認證的請求。
 * 當使用者未登入或未建立有效的 SecurityContext 就存取受保護 API 時，
 * 統一由這裡回傳 401。
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  /**
   * 回傳標準的 401 JSON 結構，避免前端收到非 JSON 回應。
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    Outbound error = Outbound.error(ResultCode.UNAUTHORIZED);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(response.getWriter(), error);
  }

}
