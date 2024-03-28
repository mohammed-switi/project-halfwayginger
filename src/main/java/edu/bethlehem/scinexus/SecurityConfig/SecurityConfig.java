package edu.bethlehem.scinexus.SecurityConfig;


import edu.bethlehem.scinexus.Authorization.AuthorizationManager;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final AuthorizationManager authorizationManager;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/auth/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/articles/{journalId}")
                                .access(authorizationManager.readJournals())
                                .requestMatchers(HttpMethod.PUT, "/articles/{journalId}")
                                .access(authorizationManager.journalOwnerContributors())
                                .requestMatchers(HttpMethod.PATCH, "/articles/{journalId}")
                                .access(authorizationManager.journalOwnerContributors())

                                .requestMatchers(HttpMethod.DELETE, "/articles/{journalId}")
                                .access(authorizationManager.journalOwner())

                                .requestMatchers("/journals/{journalId}/contributors/{contributorId}")
                                .access(authorizationManager.journalOwner())

                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        return httpSecurity.build();
    }




}