package com.example.simplecontrollertesttask.config;

import com.example.simplecontrollertesttask.controller.handler.CustomHandlerExceptionResolver;
import com.example.simplecontrollertesttask.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;
    private final CustomHandlerExceptionResolver customHandlerExceptionResolver;


    @Autowired
    public WebConfig(TokenInterceptor tokenInterceptor, CustomHandlerExceptionResolver customHandlerExceptionResolver) {
        this.tokenInterceptor = tokenInterceptor;
        this.customHandlerExceptionResolver = customHandlerExceptionResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(customHandlerExceptionResolver);
    }
}
