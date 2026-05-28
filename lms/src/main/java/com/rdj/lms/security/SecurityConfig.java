package com.rdj.lms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.http.HttpMethod;

//security/SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Autowired
 private JwtFilter jwtFilter;

 @Autowired
 private CustomUserDetailsService userDetailsService;

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http)
         throws Exception {

     http
         .csrf(csrf -> csrf.disable())

         .authorizeHttpRequests(auth -> auth

             // ── PUBLIC ROUTES ──────────────────────
             .requestMatchers("/api/auth/**").permitAll()
             .requestMatchers("/swagger-ui/**").permitAll()
             .requestMatchers("/swagger-ui.html").permitAll()
             .requestMatchers("/v3/api-docs/**").permitAll()

             // ── PUBLIC GET ROUTES ──────────────────
             .requestMatchers(
                 HttpMethod.GET,
                 "/api/courses/**").permitAll()
             .requestMatchers(
                 HttpMethod.GET,
                 "/api/reviews/**").permitAll()

             // ── PROTECTED ROUTES ───────────────────
             .anyRequest().authenticated()
         )

         .sessionManagement(session -> session
             .sessionCreationPolicy(
                 SessionCreationPolicy.STATELESS))

         .authenticationProvider(authenticationProvider())

         .addFilterBefore(jwtFilter,
             UsernamePasswordAuthenticationFilter.class);

     return http.build();
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }

 @Bean
 public AuthenticationManager authenticationManager(
         AuthenticationConfiguration config)
         throws Exception {
     return config.getAuthenticationManager();
 }

 @Bean
 public AuthenticationProvider authenticationProvider() {
     DaoAuthenticationProvider provider =
             new DaoAuthenticationProvider(userDetailsService);
     provider.setPasswordEncoder(passwordEncoder());
     return provider;
 }
}