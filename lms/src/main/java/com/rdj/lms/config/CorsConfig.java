package com.rdj.lms.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

 @Bean
 public CorsFilter corsFilter() {

     CorsConfiguration config = new CorsConfiguration();

     // who can call your API
     config.setAllowedOrigins(Arrays.asList(
         "http://localhost:3000",    // React
         "http://localhost:4200",    // Angular
         "http://localhost:8080"     // same server testing
     ));

     // allowed HTTP methods
     config.setAllowedMethods(Arrays.asList(
         "GET",
         "POST",
         "PUT",
         "DELETE",
         "OPTIONS"
     ));

     // allowed headers
     config.setAllowedHeaders(Arrays.asList(
         "Authorization",     // JWT token header
         "Content-Type",      // JSON content
         "Accept"
     ));

     // allow JWT token in header
     config.setAllowCredentials(true);

     // apply to all routes
     UrlBasedCorsConfigurationSource source =
             new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", config);

     return new CorsFilter(source);
 }
}