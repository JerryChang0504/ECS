package com.giun.ecs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 啟用 auditing 功能
@SpringBootApplication
public class EcsBackEndServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcsBackEndServiceApplication.class, args);
	}

}
