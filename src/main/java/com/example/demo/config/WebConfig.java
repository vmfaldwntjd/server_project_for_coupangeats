package com.example.demo.config;

import com.example.demo.config.interceptor.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //     제외대상
    private String[] INTERCEPTOR_WHITE_LIST = {
            "/app/users/sign-up", "/app/users", "/app/users/sign-in", "/app/events/**", "/app/categories/**", "/app/restaurants/**",
            "/test/log", "/images/**", "/app/users/{userId}/addresses", "/app/reviews/**", "/app/payments/**"
    };

    @Autowired
    JwtAuthInterceptor jai;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jai).addPathPatterns("/**").excludePathPatterns(INTERCEPTOR_WHITE_LIST);
    }

    // 이미지 전달
    @Value("${connectPath}")
    private String connectPath;
    @Value("${resourcePath}")
    private String resourcePath;

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(connectPath+"/**")
                .addResourceLocations(resourcePath);
    }
}
