package com.rdj.lms.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Autowired
 private JwtFilter jwtFilter;

 @Autowired
 private CustomUserDetailsService userDetailsService;

 // ─── SECURITY FILTER CHAIN ────────────────────────
 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http)
         throws Exception {

     http
         // enable CORS
         .cors(cors -> cors
             .configurationSource(
                 corsConfigurationSource()))

         // disable CSRF for REST API
         .csrf(csrf -> csrf.disable())

         // route permissions
         .authorizeHttpRequests(auth -> auth

             // ── PUBLIC ROUTES ──────────────────────
             .requestMatchers(
                 "/api/auth/**").permitAll()
             .requestMatchers(
                 "/swagger-ui/**").permitAll()
             .requestMatchers(
                 "/swagger-ui.html").permitAll()
             .requestMatchers(
                 "/v3/api-docs/**").permitAll()

             // ── PUBLIC GET ROUTES ──────────────────
             .requestMatchers(
                 HttpMethod.GET,
                 "/api/courses/**").permitAll()
             .requestMatchers(
                 HttpMethod.GET,
                 "/api/reviews/**").permitAll()

             // ── ALL OPTIONS REQUESTS ───────────────
             // needed for CORS preflight
             .requestMatchers(
                 HttpMethod.OPTIONS,
                 "/**").permitAll()

             // ── PROTECTED ROUTES ───────────────────
             .anyRequest().authenticated()
         )

         // stateless session — JWT based
         .sessionManagement(session -> session
             .sessionCreationPolicy(
                 SessionCreationPolicy.STATELESS))

         // custom authentication provider
         .authenticationProvider(
             authenticationProvider())

         // JWT filter before Spring auth filter
         .addFilterBefore(jwtFilter,
             UsernamePasswordAuthenticationFilter.class);

     return http.build();
 }

 // ─── CORS CONFIGURATION ───────────────────────────
 @Bean
 public CorsConfigurationSource corsConfigurationSource() {

     CorsConfiguration config = new CorsConfiguration();

     // allowed origins
     config.setAllowedOrigins(Arrays.asList(
         "http://localhost:5173",   // Vite ✅
         "http://localhost:3000",   // CRA
         "http://localhost:8080"    // same server
     ));

     // allowed methods
     config.setAllowedMethods(Arrays.asList(
         "GET",
         "POST",
         "PUT",
         "DELETE",
         "OPTIONS"    // preflight ✅
     ));

     // allowed headers
     config.setAllowedHeaders(Arrays.asList(
         "Authorization",
         "Content-Type",
         "Accept",
         "Origin",
         "X-Requested-With"
     ));

     // allow credentials
     config.setAllowCredentials(true);

     // expose headers
     config.setExposedHeaders(Arrays.asList(
         "Authorization"
     ));

     // apply to all routes
     UrlBasedCorsConfigurationSource source =
             new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", config);

     return source;
 }

 // ─── PASSWORD ENCODER ─────────────────────────────
 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }

 // ─── AUTHENTICATION MANAGER ───────────────────────
 @Bean
 public AuthenticationManager authenticationManager(
         AuthenticationConfiguration config)
         throws Exception {
     return config.getAuthenticationManager();
 }

 // ─── AUTHENTICATION PROVIDER ──────────────────────
 @Bean
 public AuthenticationProvider authenticationProvider() {
     DaoAuthenticationProvider provider =
             new DaoAuthenticationProvider(
                 userDetailsService);
     provider.setPasswordEncoder(passwordEncoder());
     return provider;
 }
}