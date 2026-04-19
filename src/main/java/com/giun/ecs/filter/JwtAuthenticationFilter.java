package com.giun.ecs.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.enums.ResultCode;
import com.giun.ecs.service.UserService;
import com.giun.ecs.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final List<String> WHITELIST = List.of(
      "/api/login", // 登入
      "/api/register", // 註冊
      "/api/refresh", // 刷新 Token
      "/swagger-ui/", // Swagger UI
      "/v3/api-docs", // OpenAPI 文件
      "/swagger-ui.html" // Swagger UI 首頁
  );

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserService userService;

  /**
   * 白名單路徑不進行 JWT 驗證，避免公開 API 被誤判為需要登入。
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return WHITELIST.stream().anyMatch(path::startsWith);
  }

  /**
   * 驗證請求中的 Bearer Token。
   * 驗證成功時將使用者資訊寫入 SecurityContext，驗證失敗時回傳 401 JSON。
   */
  @Override
  public void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Optional<String> tokenOpt = extractBearerToken(request);

      // 沒有帶 token 時，先交由後續的 Spring Security 規則判斷是否允許存取。
      // 若該 API 受保護，之後會由 AuthenticationEntryPoint 回傳 401。
      if (tokenOpt.isEmpty()) {
        filterChain.doFilter(request, response);
        return;
      }

      String token = tokenOpt.get();

      if (!jwtUtil.isAccessToken(token)) {
        writeAuthError(response, ResultCode.INVALID_TOKEN);
        return;
      }

      String username = jwtUtil.getUsernameFromToken(token);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserInfo userInfo = userService.findUserByUsername(username);
        if (userInfo == null) {
          writeAuthError(response, ResultCode.USER_IS_NOT_EXIST);
          return;
        }

        // 驗證通過後，將使用者身分與角色權限放入 SecurityContext，
        // 讓後續的 Spring Security 能進行角色授權判斷。
        if (jwtUtil.validateToken(token, userInfo)) {
          List<GrantedAuthority> authorities = List.of(
              new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().toUpperCase()));

          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userInfo, null, authorities);
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (ExpiredJwtException e) {
      SecurityContextHolder.clearContext();
      writeAuthError(response, ResultCode.TOKEN_EXPIRED);
      return;
    } catch (JwtException | IllegalArgumentException e) {
      SecurityContextHolder.clearContext();
      writeAuthError(response, ResultCode.INVALID_TOKEN);
      return;
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 從 Authorization Header 中擷取 Bearer Token。
   */
  private Optional<String> extractBearerToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");

    if (header == null) {
      return Optional.empty();
    }

    header = header.trim();

    if (header.regionMatches(true, 0, "Bearer ", 0, 7)) {
      return Optional.of(header.substring(7).trim());
    }

    return Optional.empty();
  }

  /**
   * 將 JWT 驗證失敗統一包裝成 JSON 回傳，方便前端依錯誤碼進行後續處理。
   */
  private void writeAuthError(HttpServletResponse response, ResultCode resultCode)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    OBJECT_MAPPER.writeValue(response.getWriter(), Outbound.error(resultCode));
  }
}
