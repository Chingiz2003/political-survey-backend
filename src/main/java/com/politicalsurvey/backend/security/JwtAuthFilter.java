package com.politicalsurvey.backend.security;

import com.politicalsurvey.backend.entity.Citizen;
import com.politicalsurvey.backend.repository.CitizenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CitizenRepository citizenRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, CitizenRepository citizenRepository) {
        this.jwtUtil = jwtUtil;
        this.citizenRepository = citizenRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Пропускаем auth пути и admin пути
        return path.equals("/api/auth/login") ||
                path.startsWith("/api/face/verify") ||
                path.startsWith("/api/admin/") ||
                path.startsWith("/api/intro-survey/") ||
                path.startsWith("/api/recommendation/") ||
                path.startsWith("/api/summary/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Печатаем путь запроса для отладки
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Request Method: " + request.getMethod());

            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Integer citizenId = jwtUtil.validateTokenAndGetId(token);
                    Citizen citizen = citizenRepository.findById(citizenId).orElseThrow();

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            citizen, null, Collections.emptyList());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Authenticated user with ID: " + citizenId);
                } catch (Exception e) {
                    System.out.println("Error validating JWT token: " + e.getMessage());
                    SecurityContextHolder.clearContext();
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Exception in JwtAuthFilter: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}