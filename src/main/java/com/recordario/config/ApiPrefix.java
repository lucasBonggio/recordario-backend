package com.recordario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiPrefix implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configuracion){
        configuracion.addPathPrefix(
            "/api/v1",
            clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
