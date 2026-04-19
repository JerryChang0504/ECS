package com.giun.ecs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * OpenAPI 3.x（Swagger UI）設定。必須指定 {@link OpenAPI#openapi(String)}， 否則 Swagger UI 會出現「does not
 * specify a valid version field」錯誤。
 *
 * <p>
 * 此處使用 OpenAPI <strong>3.1.0</strong>（規格根欄位 {@code openapi: "3.1.0"}）， 與 Springdoc 2.8.x 內嵌之
 * Swagger UI 相容。
 */
@Configuration
public class SwaggerConfig {

  /** OpenAPI 規格版本（Swagger 3 / OpenAPI 3.1） */
  public static final String OPENAPI_SPEC_VERSION = "3.1.0";

  private static final String TITLE = "ECS API";
  private static final String DESCRIPTION = "RESTful API 文件（含 JWT Bearer 認證）";
  /** API 文件本身的版本號（與 OpenAPI 規格版本 openapi 不同） */
  public static final String API_DOC_VERSION = "1.0.0";

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .openapi(OPENAPI_SPEC_VERSION)
        .info(new Info()
            .title(TITLE)
            .description(DESCRIPTION)
            .version(API_DOC_VERSION)
            .contact(new Contact().name("開發者")
                .email("dev@example.com")
                .url("https://example.com"))
            .license(new License().name("MIT")
                .url("https://opensource.org/licenses/MIT")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
