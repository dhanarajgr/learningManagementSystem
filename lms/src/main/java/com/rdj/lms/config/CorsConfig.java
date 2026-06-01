package com.rdj.lms.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//config/CorsConfig.java
@Configuration
public class CorsConfig {

 @Bean
 public CorsFilter corsFilter() {

     CorsConfiguration config = new CorsConfiguration();

     // ── ADD VITE PORT ──────────────────────────────
     config.setAllowedOrigins(Arrays.asList(
         "http://localhost:5173",   // Vite ✅
         "http://localhost:3000",   // CRA
         "http://localhost:8080"    // same server
     ));

     config.setAllowedMethods(Arrays.asList(
         "GET",
         "POST",
         "PUT",
         "DELETE",
         "OPTIONS"    // ← must include OPTIONS ✅
     ));

     config.setAllowedHeaders(Arrays.asList(
         "Authorization",
         "Content-Type",
         "Accept",
         "Origin",
         "X-Requested-With"
     ));

     config.setAllowCredentials(true);

     // ── EXPOSE HEADERS ────────────────────────────
     config.setExposedHeaders(Arrays.asList(
         "Authorization"
     ));

     UrlBasedCorsConfigurationSource source =
             new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", config);

     return new CorsFilter(source);
 }
}