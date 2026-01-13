//package com.dulara.figure_controller.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public String generateToken(UserDetails userDetails, Long userId, String role,
//                                Long regionId, Long branchId) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", userId);
//        claims.put("role", role);
//        claims.put("regionId", regionId);
//        claims.put("branchId", branchId);
//
//        return createToken(claims, userDetails.getUsername());
//    }
//
//    public String createToken(Map<String, Object> claims, String subject) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .claims(claims)
//                .subject(subject)
//                .issuedAt(now)
//                .expiration(expiryDate)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Long extractUserId(String token) {
//        return extractClaim(token, claims -> claims.get("userId", Long.class));
//    }
//
//    public String extractRole(String token) {
//        return extractClaim(token, claims -> claims.get("role", String.class));
//    }
//
//    public Long extractRegionId(String token) {
//        return extractClaim(token, claims -> claims.get("regionId", Long.class));
//    }
//
//    public Long extractBranchId(String token) {
//        return extractClaim(token, claims -> claims.get("branchId", Long.class));
//    }
//
//
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}
package com.dulara.figure_controller.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmpType(String token) {
        return extractClaim(token, claims -> claims.get("empType", String.class));
    }

    public String extractSfcCode(String token) {
        return extractClaim(token, claims -> claims.get("sfcCode", String.class));
    }

    public String extractBranchCode(String token) {
        return extractClaim(token, claims -> claims.get("branchCode", String.class));
    }

    public String extractBranchName(String token) {
        return extractClaim(token, claims -> claims.get("branchName", String.class));
    }

    public String extractRegionCode(String token) {
        return extractClaim(token, claims -> claims.get("regionCode", String.class));
    }

    public String extractRegionName(String token) {
        return extractClaim(token, claims -> claims.get("regionName", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}