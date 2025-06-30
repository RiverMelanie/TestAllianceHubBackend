package com.neucamp.testalliancehubbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射视频文件路径
        registry.addResourceHandler("/uploads/videos/**")
                .addResourceLocations("file:./uploads/videos/")
                .setCachePeriod(0);

        // 映射图片文件路径
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:./uploads/images/")
                .setCachePeriod(0);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}