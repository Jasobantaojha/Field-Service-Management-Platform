package com.KeyStone.FieldServiceProject.Security;

import java.security.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.KeyStone.FieldServiceProject.ENUM.Permissions;
import com.KeyStone.FieldServiceProject.Entity.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    private final SecretKey key;

    // Token valid for 12 hours
    private final long validTokenTime = 12 * 60 * 60 * 1000L;

    public JWTUtil() {

        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.isEmpty()) {
            secret = "Replace this place with secret code";
        }

        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserAuth user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole().name());

        Set<Permissions> perm = RoleBasedPermissions
                .getRoleBasedPermission()
                .get(user.getRole().name());

        Date now = new Date();

        Date expire = new Date(
                now.getTime() + validTokenTime
        );

        return Jwts.builder()
               .claims(claims)
               .subject(user.getUserEmail())
                .issuedAt(now)
                .expiration(expire)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public Claims getClaim(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserEmail(String token) {

        return getClaim(token).getSubject();
    }

    public String extractToken(String header) {

        return null;
    }
}