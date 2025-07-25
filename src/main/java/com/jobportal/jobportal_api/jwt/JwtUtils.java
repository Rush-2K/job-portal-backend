package com.jobportal.jobportal_api.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // get/extract jwt token from httpHeader
    public String getJwtFromHeader(HttpServletRequest request) {
        // header name is Authorization
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }

    // generate token from the username
    // public String generateTokenFromUsername(UserDetails userDetails, String
    // userId) {
    // String username = userDetails.getUsername();
    // // building the token an setting the issue time, expiration time, signing it
    // // with a key
    // return Jwts.builder()
    // .setSubject(username)
    // .claim("role",
    // userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_",
    // ""))
    // .claim("uid", userId)
    // .issuedAt(new Date())
    // .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
    // .signWith(key())
    // .compact();
    // }

    // to get username from the token or "decoding"
    // public String getUserNameFromJwtToken(String token) {
    // return Jwts.parser()
    // .verifyWith((SecretKey) key())
    // .build().parseSignedClaims(token)
    // .getPayload().getSubject();
    // }

    // key for signing jwt
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // to validate jwt token
    // public boolean validateJwtToken(String authToken) {
    // try {
    // System.out.println("Validate");
    // Jwts.parserBuilder().setSigningKey((SecretKey)
    // key()).build().parseSignedClaims(authToken);
    // return true;
    // } catch (MalformedJwtException e) {
    // logger.error("Invalid JWT token: {}", e.getMessage());
    // } catch (ExpiredJwtException e) {
    // logger.error("JWT token is expired: {}", e.getMessage());
    // } catch (UnsupportedJwtException e) {
    // logger.error("JWT token is unsupported: {}", e.getMessage());
    // } catch (IllegalArgumentException e) {
    // logger.error("JWT claims string is empty: {}", e.getMessage());
    // }
    // return false;
    // }

    // get email/username from security context
    public String getUserId() {
        return (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials();
    }

    // generate token together with uid and roles
    public String generateToken(String username, Long userId, String roles) {
        Date now = new Date();
        Date exp = new Date((new Date()).getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("uid", userId)
                .claim("role", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key())
                .compact();
    }

    public Claims claims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return claims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return claims(token).get("uid", Long.class);
    }

    public String extractRoles(String token) {
        return claims(token).get("role", String.class);
    }

}