package edu.bethlehem.scinexus.SecurityConfig;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final AuthorizationManager authorizationManager;
        private final CustomJwtDecoder customJwtDecoder;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(AbstractHttpConfigurer::disable)

                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/index.html").permitAll()
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers("/js/**").permitAll()
                                                .requestMatchers("/ws/**").permitAll()
                                                .requestMatchers("/topic/public").permitAll()
                                                .requestMatchers("/app/**").permitAll()
                                                .requestMatchers("/actuator/**").permitAll()
                                                // Academics
                                                .requestMatchers(HttpMethod.GET, "/academics")
                                                .access(authorizationManager.admin())

                                                .requestMatchers(HttpMethod.PATCH, "/academics/{userId}")
                                                .access(authorizationManager.userHimSelfAndAdmin())

                                                // Articles
                                                .requestMatchers(HttpMethod.GET, "/articles")
                                                .access(authorizationManager.admin())

                                                // .requestMatchers(HttpMethod.POST, "/articles/")
                                                // .access(new AuthorizationDecision(true))

                                                .requestMatchers(HttpMethod.GET, "/articles/{journalId}")
                                                .access(authorizationManager.readJournals())

                                                .requestMatchers(HttpMethod.PATCH, "/articles/{journalId}")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/articles/{journalId}")
                                                .access(authorizationManager.journalOwner())

                                                // interactions
                                                .requestMatchers(HttpMethod.DELETE, "/interactions/{interactionId}")
                                                .access(authorizationManager.interactionOwner())

                                                .requestMatchers(HttpMethod.PATCH, "/interactions/{interactionId}")
                                                .access(authorizationManager.interactionOwner())

                                                .requestMatchers(HttpMethod.POST, "/interactions/journal/{journalId}")
                                                .access(authorizationManager.readJournals())
                                                // Finished Testing Till here only
                                                // ===============================================================================^^^^^^^^^^^^^^^^Tested^^^^^^^^
                                                // Journals
                                                .requestMatchers(HttpMethod.GET,
                                                                "/journals")
                                                .access(authorizationManager.admin())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/journals/{journalId}")
                                                .access(authorizationManager.readJournals())

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/journals/{journalId}/contributors/{contributorId}")
                                                .access(authorizationManager.journalOwnerNew())

                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/journals/{journalId}/contributors/{contributorId}")
                                                .access(authorizationManager.journalOwnerNew())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/journals/{journalId}/interactions")
                                                .access(authorizationManager.readJournals())

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/journals/{journalId}/media")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.POST,
                                                                "/journals/{journalId}/media")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/journals/{journalId}/opinions")
                                                .access(authorizationManager.readJournals())

                                                // Media
                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/medias/{mediaId}")
                                                .access(authorizationManager.mediaOwner())

                                                // Notifications
                                                .requestMatchers(HttpMethod.GET,
                                                                "/notifications/")
                                                .access(authorizationManager.admin())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/notifications/{notificaitionId}")
                                                .access(authorizationManager.admin())

                                                // Opinions
                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/opinions/{opinionId}")
                                                .access(authorizationManager.opinionOwner())

                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/opinions/{opinionId}")
                                                .access(authorizationManager.opinionOwner())

                                                // Organizations
                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/organizations/{userId}")
                                                .access(authorizationManager.userHimSelfAndAdmin())

                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/organizations/{userId}")
                                                .access(authorizationManager.userHimSelfAndAdmin())

                                                // Posts
                                                .requestMatchers(HttpMethod.GET,
                                                                "/posts/")
                                                .access(authorizationManager.admin())

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/posts/{journalId}")
                                                .access(authorizationManager.journalOwner())

                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/posts/{journalId}")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/posts/{journalId}")
                                                .access(authorizationManager.readJournals())

                                                // researchpapers
                                                .requestMatchers(HttpMethod.GET,
                                                                "/researchpapers/")
                                                .access(authorizationManager.admin())

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/researchpapers/{journalId}")
                                                .access(authorizationManager.journalOwner())

                                                .requestMatchers(HttpMethod.PATCH,
                                                                "/researchpapers/{journalId}")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/researchpapers/{journalId}")
                                                .access(authorizationManager.readJournals())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/researchpapers/{journalId}/access/**")
                                                .access(authorizationManager.journalOwnerContributors())

                                                // Users

                                                .requestMatchers(HttpMethod.GET,
                                                                "/users")
                                                .access(authorizationManager.admin())

                                                .anyRequest().authenticated())

                                .formLogin(httpSecurityFormLoginConfigurer -> {
                                        httpSecurityFormLoginConfigurer.loginPage("/api/v1/auth/login").permitAll();
                                        httpSecurityFormLoginConfigurer.loginProcessingUrl("/api/v1/auth/login")
                                                        .permitAll();
                                        httpSecurityFormLoginConfigurer.successForwardUrl("/dashboard");
                                        httpSecurityFormLoginConfigurer.usernameParameter("email");
                                        httpSecurityFormLoginConfigurer.passwordParameter("password");
                                })

                                // .oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) ->
                                // {jwt.decoder(jwtDecoder());}))

                                // .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                                // httpSecurityOAuth2LoginConfigurer.loginPage("/api/v1/auth/login").permitAll();

                                // httpSecurityOAuth2LoginConfigurer.tokenEndpoint(tokenEndpointConfig ->
                                // tokenEndpointConfig.accessTokenResponseClient(
                                // accessTokenResponseClient()
                                // ));

                                // httpSecurityOAuth2LoginConfigurer.defaultSuccessUrl("/dashboard",true);
                                // }
                                // )
                                .sessionManagement(sessionConfigurer -> sessionConfigurer
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                return customJwtDecoder;
        }

        @Bean
        public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
                return new DefaultAuthorizationCodeTokenResponseClient();
        }

        @Bean
        public RestTemplate restTemplate() {
                return new RestTemplateBuilder().build();
        }

        // @Bean
        // public Customizer<CorsConfigurer<HttpSecurity>> corsFilter() {
        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true);
        // config.addAllowedOrigin("*"); // Allow all origins
        // config.addAllowedHeader("*"); // Allow all headers
        // config.addAllowedMethod("*"); // Allow all methods
        // source.registerCorsConfiguration("/**", config);
        // return (Customizer<CorsConfigurer<HttpSecurity>>) new CorsFilter(source);
        // }
        // @Bean
        // public Customizer<CorsConfigurer<HttpSecurity>> corsFilter() {
        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true);
        // config.addAllowedOrigin("*"); // Allow all origins
        // config.addAllowedHeader("*"); // Allow all headers
        // config.addAllowedMethod("*"); // Allow all methods
        // source.registerCorsConfiguration("/**", config);
        // return (Customizer<CorsConfigurer<HttpSecurity>>) new CorsFilter(source);
        // }

}