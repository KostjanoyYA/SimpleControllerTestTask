package com.example.simplecontrollertesttask.controller;

import com.example.simplecontrollertesttask.controller.handler.CustomHandlerExceptionResolver;
import com.example.simplecontrollertesttask.dto.ResponseDto;
import com.example.simplecontrollertesttask.dto.ResultDto;
import com.example.simplecontrollertesttask.interceptor.TokenInterceptor;
import com.example.simplecontrollertesttask.service.ResultService;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResultController.class)
@TestPropertySource(properties = {
        "security.hashing.key=1234567890AbCdEfG",
        "security.hashing.algorithm=HmacSHA256"
})
public class ResultControllerTest {
    private static final MultiValueMap<String, String> MULTI_VALUE_MAP = new LinkedMultiValueMap<>();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultService resultService;

    @SpyBean
    private CustomHandlerExceptionResolver resolver;

    @MockBean
    private TokenInterceptor interceptor;

    @BeforeAll
    static void beforeAll() {
        MULTI_VALUE_MAP.add("key", "value");
    }

    @BeforeEach
    void setUp() {
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(resolver.resolveException(any(), any(), any(), any())).thenCallRealMethod();
    }

    @AfterEach
    void tearDown() {
        reset(interceptor);
    }

    @Test
    public void getResult_Successful_ReturnsResponseDto() throws Exception {
        ResponseDto responseDto = new ResponseDto("success", List.of(new ResultDto("signature")));
        when(resultService.getResponse(any())).thenReturn(responseDto);

        mockMvc.perform(post("/result/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(MULTI_VALUE_MAP))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.result[0].signature").value("signature"));
    }

    @Test
    public void getResult_InvalidOperationId_ReturnsBadRequest() {
        ServletException ex = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(post("/result/-1")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .params(MULTI_VALUE_MAP))
                        .andExpect(status().isBadRequest())
        );
        assertNotNull(ex);
        assertTrue(ex.getRootCause() instanceof ConstraintViolationException);
        assertTrue(ex.getRootCause().getMessage().contains("must be greater than 0"));
    }

    @Test
    public void getResult_EmptyBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/result/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnsupportedMediaType());
    }
}
