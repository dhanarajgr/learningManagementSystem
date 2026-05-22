package com.rdj.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

//config/SwaggerConfig.java
@Configuration
public class SwaggerConfig {

 @Bean
 public OpenAPI openAPI() {
     return new OpenAPI()

         // ─── API Info ─────────────────────────────
         .info(new Info()
             .title("Learning Management System API")
             .description("REST API for LMS — " +
                 "Courses, Lessons, Enrollments, Reviews")
             .version("1.0.0")
         )

         // ─── JWT Security Scheme ──────────────────
         // adds Authorize button in Swagger UI
         .addSecurityItem(new SecurityRequirement()
             .addList("Bearer Authentication"))

         .components(new Components()
             .addSecuritySchemes("Bearer Authentication",
                 new SecurityScheme()
                     .type(SecurityScheme.Type.HTTP)
                     .scheme("bearer")
                     .bearerFormat("JWT")
                     .description("Enter JWT token here")
             )
         );
 }
}