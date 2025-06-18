package com.zsl.bookai.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor("gydeuwabfhyiuwegvyufiwebfhuewjvfrywenhjoii".getBytes());
    private final JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

    public String validateToken(String token) {
        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            return claims.getSubject(); // 返回用户名
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1天
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
}
