package com.bklg.csvdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  
  /**
   * CORS対策で、フロントエンドからのapi呼び出しを許可する
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("http://localhost")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
  }
}