package com.giun.ecs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger類別
 */

@Configuration
@Profile("!prod") // 除了生產環境外才載入
public class SwaggerConfig {
  private static final String TITLE = "會員系統 API 文件";
  private static final String DESCRIPTION = "RESTful API 文件說明，提供會員基本操作";
  public static final String VERSION = "v.1.20240520.01";

  @Bean
  public OpenAPI customOpenAPI() {

    return new OpenAPI()
        .info(new Info()
            .title(TITLE).description(DESCRIPTION)
            .version(VERSION)
            .contact(new Contact().name("開發者")
                .email("dev@example.com")
                .url("https://example.com"))
            .license(new License().name("MIT")
                .url("https://opensource.org/licenses/MIT")))
        .addSecurityItem(
            new SecurityRequirement().addList("bearerAuth"))
        .components(new Components().addSecuritySchemes("bearerAuth",
            new SecurityScheme().name("Authorization")
                .type(SecurityScheme.Type.HTTP).scheme("bearer")
                .bearerFormat("JWT")));
  }

}
