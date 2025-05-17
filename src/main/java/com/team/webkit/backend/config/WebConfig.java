package com.team.webkit.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/")
            .setCachePeriod(3600)
            .resourceChain(true);
        System.out.println("정적 리소스 경로: " + System.getProperty("user.dir") + "/uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/uploads/**")
            .allowedOrigins("*")
            .allowedMethods("GET")
            .allowedHeaders("*")
            .allowedOrigins("http://localhost:5173")
            .exposedHeaders("Content-Type", "Content-Length");

        // ← 여기부터 추가된 부분입니다 →
        // 2) /api/** 전체에 대해 React(5173)에서 오는 모든 메서드 허용
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);

    }


}
