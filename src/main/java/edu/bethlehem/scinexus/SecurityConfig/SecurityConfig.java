package edu.bethlehem.scinexus.SecurityConfig;


import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(HttpRequestAuthorizer -> HttpRequestAuthorizer
                                                .requestMatchers("/api/v1/auth/**")
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(sessionConfigurer -> sessionConfigurer
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                // .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                // httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new
                // JwtAuthenticationEntryPoint());
                // });

                return httpSecurity.build();
        }

}
