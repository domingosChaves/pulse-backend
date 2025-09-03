package com.domingos.pulse_backend.config;

import feign.RequestInterceptor;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor headerForwardingInterceptor() {
        return requestTemplate -> {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                HttpServletRequest request = servletAttrs.getRequest();
                String auth = request.getHeader("Authorization");
                String corr = request.getHeader("X-Correlation-ID");
                if (auth != null) requestTemplate.header("Authorization", auth);
                if (corr != null) requestTemplate.header("X-Correlation-ID", corr);
            }
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
}

