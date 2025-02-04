package org.example.app.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.app.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            logger.info("CORS Preflight request detected, allowing through.");
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getRequestURI();
        if (path.equals("/register") || path.equals("/login")) {
            logger.info("Skipping jwt validation for {}", path);
            filterChain.doFilter(request, response); // Skip JWT validation for these endpoints
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Missing Auth header");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing Auth header");
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            if (username == null || !jwtUtil.validateToken(jwt, username)) {
                logger.error("Invalid JWT");
                throw new JwtException("Invalid JWT");
            }
            List<RoleType> roles = jwtUtil.extractRoles(jwt);
            request.setAttribute("username", username);
            request.setAttribute("roles", roles);

        } catch (ExpiredJwtException e) {
            logger.error("JWT validation failed: ", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Expired JWT token. Pls login again");
            return;
        } catch (Exception e) {
            logger.error("JWT validation failed: ", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid JWT token. Pls login again");
            return;
        }
        logger.error("filter passed");
        filterChain.doFilter(request, response);
    }
}