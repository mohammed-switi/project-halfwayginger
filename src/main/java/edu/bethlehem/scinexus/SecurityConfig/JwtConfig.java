package edu.bethlehem.scinexus.SecurityConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return new CustomJwtDecoder(new JwtService()); // Instantiate CustomJwtDecoder with JwtService dependency
    }
}
