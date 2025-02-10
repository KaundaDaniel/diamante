package com.hotel.diamante.security;

import com.hotel.diamante.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthFilter {
    private final JWTUtils jwtUtils;
    private final CachingUserDetailsService cachingUserDetailsService;

    public JWTAuthFilter(JWTUtils jwtUtils, CachingUserDetailsService cachingUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.cachingUserDetailsService = cachingUserDetailsService;
    }
    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){

    }
}
