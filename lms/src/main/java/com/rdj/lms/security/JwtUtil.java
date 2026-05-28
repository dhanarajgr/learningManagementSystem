package com.rdj.lms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

//security/JwtUtil.java
@Component
public class JwtUtil {

 @Value("${app.jwt.secret}")
 private String secret;

 @Value("${app.jwt.expiration}")
 private long expiration;

 // ─── Generate Token ───────────────────────────────
 public String generateToken(String email) {
     return Jwts.builder()
         .subject(email)
         .issuedAt(new Date())
         .expiration(new Date(
             System.currentTimeMillis() + expiration))
         .signWith(getSignKey())
         .compact();
 }

 // ─── Get Email From Token ─────────────────────────
 public String getEmailFromToken(String token) {
     return getClaims(token).getSubject();
 }

 // ─── Validate Token ───────────────────────────────
 public boolean isTokenValid(String token, String email) {
     String tokenEmail = getEmailFromToken(token);
     return tokenEmail.equals(email) && !isTokenExpired(token);
 }

 // ─── Check Expiry ─────────────────────────────────
 private boolean isTokenExpired(String token) {
     return getClaims(token)
             .getExpiration()
             .before(new Date());
 }

 // ─── Get Claims ───────────────────────────────────
 private Claims getClaims(String token) {
     return Jwts.parser()
         .verifyWith(getSignKey())
         .build()
         .parseSignedClaims(token)
         .getPayload();
 }

 // ─── Get Sign Key ─────────────────────────────────
 private SecretKey getSignKey() {
	    byte[] keyBytes = secret.getBytes();
	    return Keys.hmacShaKeyFor(keyBytes);
	}
}