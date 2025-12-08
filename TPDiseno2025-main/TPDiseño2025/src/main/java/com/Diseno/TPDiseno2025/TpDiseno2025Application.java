package com.Diseno.TPDiseno2025;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class TpDiseno2025Application {
	public static void main(String[] args) {
		SpringApplication.run(TpDiseno2025Application.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer(){
			@Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") 
                        .allowedOrigins("http://localhost:3000") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") 
                        .allowCredentials(true);
            }
		};
	}

	@PostConstruct
    public void init() {
        // Forzamos a que la aplicación use UTC para evitar el error de "America/Buenos_Aires"
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
