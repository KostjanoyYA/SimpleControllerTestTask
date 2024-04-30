package com.example.simplecontrollertesttask.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final String TOKEN_HEADER_NAME = "Token";
    private final String validToken;

    public TokenInterceptor(@Value("${security.token}") String validToken) {
        this.validToken = validToken;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(TOKEN_HEADER_NAME);
        if (token == null || !token.equals(validToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
