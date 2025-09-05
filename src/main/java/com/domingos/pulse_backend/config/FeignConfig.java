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

/**
 * Configurações comuns do Feign.
 * - Repassa cabeçalhos relevantes da requisição de entrada (ex.: Authorization, X-Correlation-ID)
 * - Define nível de log básico para chamadas Feign
 * - Registra um ErrorDecoder customizado
 */
@Configuration
public class FeignConfig {

    /**
     * Interceptor que repassa cabeçalhos da requisição atual para o cliente Feign.
     */
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

    /**
     * Define o nível de log do Feign (BASIC = método, URL, status e tempo).
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * Decoder de erros customizado para traduzir respostas 4xx/5xx.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
