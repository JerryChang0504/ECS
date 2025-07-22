package com.giun.ecs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.giun.ecs.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 如果你使用 Spring Security 配置，可以加上這段
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/login", "/swagger-ui/**",
								"/v3/api-docs/**", "/swagger-ui.html")
						.permitAll().anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class);;
		return http.build();
	}
}
