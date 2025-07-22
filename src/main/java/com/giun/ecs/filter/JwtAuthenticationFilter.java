package com.giun.ecs.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.service.UserService;
import com.giun.ecs.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Override
	public void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				String username = jwtUtil.getUsernameFromToken(token);

				// 若使用者尚未被驗證
				if (username != null && SecurityContextHolder.getContext()
						.getAuthentication() == null) {
					UserInfo userInfo = userService
							.findUserByUsername(username);

					if (jwtUtil.validateToken(token, userInfo)) {
						List<GrantedAuthority> authorities = List
								.of(new SimpleGrantedAuthority("ROLE_"
										+ userInfo.getRole().toUpperCase()));

						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userInfo, null, authorities);

						authToken
								.setDetails(new WebAuthenticationDetailsSource()
										.buildDetails(request));
						SecurityContextHolder.getContext()
								.setAuthentication(authToken);
					}
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token 無效或過期");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}
