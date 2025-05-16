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

    }
}
