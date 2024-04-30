package com.example.simplecontrollertesttask.controller.handler;

import com.example.simplecontrollertesttask.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper mapper;

    @Autowired
    public CustomHandlerExceptionResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(ex instanceof HttpMediaTypeNotSupportedException || ex instanceof HttpMessageNotReadableException)) return null;
        try {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(new ErrorDto(ex.getMessage())));
            response.getWriter().flush();
        } catch (Exception e) {
            log.error(
                    "Cannot handle {} ({}). Reason: {} (request={})",
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    e.getMessage(),
                    request
            );
        }
        return new ModelAndView();
    }
}
