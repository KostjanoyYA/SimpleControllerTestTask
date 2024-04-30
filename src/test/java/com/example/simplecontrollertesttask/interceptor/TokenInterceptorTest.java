package com.example.simplecontrollertesttask.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TokenInterceptorTest {
    private static final String VALID_TOKEN = "validToken";

    private HttpServletRequest request;

    private HttpServletResponse response;

    private TokenInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TokenInterceptor(VALID_TOKEN);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void preHandleValidTokenTest() {
        when(request.getHeader("Token")).thenReturn(VALID_TOKEN);
        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandleInvalidTokenTest() {
        when(request.getHeader("Token")).thenReturn("invalidToken");
        assertFalse(interceptor.preHandle(request, response, new Object()));
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
