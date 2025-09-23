package br.com.codigocomcafe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // origem exata do Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // inclua OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
