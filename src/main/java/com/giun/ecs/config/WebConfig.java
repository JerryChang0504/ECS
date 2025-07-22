package com.giun.ecs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")// 要允許跨域的路徑
						.allowedHeaders("*") // 要允許跨域的請求頭
						.allowedMethods("GET", "POST", "PUT", "DELETE") // 要允許跨域的請求方法
						.allowedOrigins("http://localhost:5173") // 要允許跨域的網域
						.allowCredentials(true); // 是否允許請求帶有憑證
			}
		};
	}

}
