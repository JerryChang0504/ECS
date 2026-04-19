package com.giun.ecs.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.giun.ecs.entryPoint.JwtAccessDeniedHandler;
import com.giun.ecs.entryPoint.JwtAuthenticationEntryPoint;
import com.giun.ecs.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  private JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 安全過濾器鏈配置
   * 
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 開啟
                                                                           // CORS
                                                                           // 支援
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 禁用
                                                                                      // session，使用
                                                                                      // JWT 驗證
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/login", "/api/register", "/api/refresh",
                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html") // 白名單
            .permitAll() // 允許所有
            .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
            .requestMatchers("/api/products/manage", "/api/addProducts",
                "/api/updateProducts/**", "/api/deleteProducts/**")
            .hasRole("ADMIN")
            .anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)// 設定JWT驗證過濾器
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 設定認證失敗處理器
            .accessDeniedHandler(jwtAccessDeniedHandler));// 設定權限不足處理器


    return http.build();
  }

  /**
   * CORS 意思是跨來源資源共享（Cross-Origin Resource Sharing），是一種瀏覽器安全機制，允許或限制來自不同來源的網頁對資源的訪問。以下是 CORS 配置的說明：
   * CORS 配置 允許來自 http://localhost:5173 的請求，並允許常見的 HTTP 方法和所有標頭。這樣前端應用就能正常與後端 API 進行跨域通信。
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("http://localhost:5173")); // 或List.of("http://localhost:5173")
    config
        .setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
