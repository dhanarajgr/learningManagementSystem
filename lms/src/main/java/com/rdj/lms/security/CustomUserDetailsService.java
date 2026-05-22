package com.rdj.lms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rdj.lms.entity.User;
import com.rdj.lms.repository.UserRepository;

//security/CustomUserDetailsService.java
@Service
public class CustomUserDetailsService implements UserDetailsService {

 @Autowired
 private UserRepository userRepository;

 @Override
 public UserDetails loadUserByUsername(String email)
         throws UsernameNotFoundException {

     User user = userRepository.findByEmail(email)
         .orElseThrow(() -> new UsernameNotFoundException(
             "User not found with email: " + email));

     return org.springframework.security.core.userdetails.User
         .withUsername(user.getEmail())
         .password(user.getPassword())
         .roles(user.getRole().name())
         .build();
 }
}