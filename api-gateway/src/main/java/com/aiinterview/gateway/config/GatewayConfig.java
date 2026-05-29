package com.aiinterview.gateway.config;

import java.util.List;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {
    @Bean KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-User-Id")).switchIfEmpty(Mono.just(exchange.getRequest().getRemoteAddress() == null ? "anonymous" : exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()));
    }
    @Bean CorsWebFilter corsWebFilter() {
        var config = new CorsConfiguration(); config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173")); config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); config.setAllowedHeaders(List.of("*")); config.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**", config); return new CorsWebFilter(source);
    }
}
