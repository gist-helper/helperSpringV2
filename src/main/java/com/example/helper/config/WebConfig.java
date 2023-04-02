package com.example.helper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("https://gdsc-timetable.vercel.app", "http://localhost:8080")
                // 생략시 wild card ("*")
                .allowedMethods("*")
                .allowCredentials(false);
//                .allowCredentials(true); true면 쿠키 요청을 금지해 보안상 이슈 발생 가능.
    }
}
