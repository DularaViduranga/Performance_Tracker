//package com.dulara.figure_controller.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.lang.Collections;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        log.debug("Authorization header: {}", authHeader);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            log.debug("No valid Authorization header found");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            final String jwt = authHeader.substring(7);
//            final String username = jwtUtil.extractUsername(jwt);
//
//            log.debug("Extracted username from token: {}", username);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                if (Boolean.TRUE.equals(isTokenValid(jwt))) {  // We'll add this method
//
//                    // Create authentication with custom authorities from claims if needed
//                    var authToken = new UsernamePasswordAuthenticationToken(
//                            username,
//                            null,
//                            List.of()  // You can extract roles from JWT claims later
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                    log.debug("Authenticated user: {}", username);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Cannot set user authentication: ", e);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//
//    private Boolean isTokenValid(String token) {
//        try {
//            return !jwtUtil.extractExpiration(token).before(new Date());
//            // Signature is automatically verified in extractExpiration()
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
package com.dulara.figure_controller.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token → continue as unauthenticated (public endpoints still work)
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            // This call validates signature + expiration. Throws if invalid/expired
            Claims claims = jwtUtil.extractAllClaims(jwt);

            String username = claims.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Token is valid → authenticate user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList() // You can add authorities from claims later
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Successfully authenticated user: {}", username);
            }

        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired for user: {}", e.getClaims().getSubject());
            sendUnauthorized(response, "Token expired");
            return;

        } catch (JwtException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            sendUnauthorized(response, "Invalid token");
            return;

        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            sendUnauthorized(response, "Token processing error");
            return;
        }

        // Token valid → proceed
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}