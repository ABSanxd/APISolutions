package com.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// Ya no se necesita WebMvcConfigurer

@Configuration
public class WebConfig {

    /**
     * Esta es la configuración de CORS que Spring Security (con .cors(withDefaults()))
     * y Spring MVC buscarán automáticamente.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos (tu frontend de Angular)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Métodos permitidos (¡Añadimos PATCH!)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Cabeceras permitidas
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales (importante para JWT en headers)
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas las rutas
        
        return source;
    }
}