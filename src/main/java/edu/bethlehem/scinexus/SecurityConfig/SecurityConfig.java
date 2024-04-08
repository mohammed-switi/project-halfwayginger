package edu.bethlehem.scinexus.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.JwtBearerOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;
import edu.bethlehem.scinexus.User.UserService;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final AuthorizationManager authorizationManager;
        // private final CustomJwtDecoder customJwtDecoder;
        // private final WebClient userClientInfo;

        private final JwtDecoder jwtDecoder;
        private final UserService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity // .cors(Customizer.withDefaults())
                             // .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new
                             // HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                                .csrf(AbstractHttpConfigurer::disable)

                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/index.html").permitAll()
                                                .requestMatchers("/privateChat.html").permitAll()
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers("/js/**").permitAll()
                                                .requestMatchers("/ws/**").permitAll()
                                                .requestMatchers("/chat").permitAll()
                                                .requestMatchers("/user/**").permitAll()
                                                .requestMatchers("/messages/{senderId}/{recipientId}").permitAll()
                                                .requestMatchers("/chat.sendMessage").permitAll()
                                                .requestMatchers("/chat.addUser").permitAll()
                                                .requestMatchers("/topic/public").permitAll()
                                                .requestMatchers("/user/public").permitAll()
                                                .requestMatchers("/user.addUser").permitAll()
                                                .requestMatchers("/user.disconnectUser").permitAll()
                                                .requestMatchers("/connected-users").permitAll()

                                                .requestMatchers("/app/**").permitAll()
                                                .requestMatchers("/actuator/**").permitAll()
                                                .requestMatchers("/oauth2/authorization/google").permitAll()

                                                .requestMatchers("/swagger-ui.html").permitAll()
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

                                                .requestMatchers(HttpMethod.POST,
                                                                "/interactions/journal/{journalId}")
                                                .access(authorizationManager.readJournals())
                                                // Finished Testing Till here only
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

                                                .requestMatchers("/api/v1/auth/login").permitAll()
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

                                                .requestMatchers(HttpMethod.POST,
                                                                "/journals/{journalId}/reshare")
                                                .access(authorizationManager.readJournals())

                                                // Media
                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/medias/{mediaId}")
                                                .access(authorizationManager.mediaOwner())

                                                .requestMatchers(HttpMethod.GET,
                                                                "/medias")
                                                .access(authorizationManager.admin())

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

                                                .requestMatchers(HttpMethod.POST,
                                                                "/researchpapers/{journalId}/access/**")
                                                .access(authorizationManager.journalOwnerContributors())
                                                // ^^^^^^^^^^^^^^^^Tested^^^^^^^^^^^^^^^^

                                                // Users

                                                .requestMatchers(HttpMethod.GET,
                                                                "/users")
                                                .access(authorizationManager.admin())

                                                .anyRequest().permitAll())

                                .sessionManagement(sessionConfigurer -> sessionConfigurer
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(loginConfig -> {
                                        loginConfig.loginPage("/api/v1/auth/login").permitAll();
                                        loginConfig.loginProcessingUrl("/api/v1/auth/login").permitAll();
                                        loginConfig.successForwardUrl("/dashboard");
                                        loginConfig.usernameParameter("email");
                                        loginConfig.passwordParameter("password");
                                })
                                .oauth2ResourceServer(oauth2Config -> oauth2Config.jwt(jwt -> jwt.decoder(jwtDecoder)))//
                                .oauth2Login(oauth2LoginConfig -> {
                                        oauth2LoginConfig.loginPage("/api/v1/auth/login").permitAll();
                                        oauth2LoginConfig.defaultSuccessUrl("/dashboard", true);
                                })

                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
        }

        @Bean
        public OAuth2AuthorizedClientProvider jwtBearer() {
                return new JwtBearerOAuth2AuthorizedClientProvider();
        }

        @Bean
        public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
                return new DefaultAuthorizationCodeTokenResponseClient();
        }

        @Bean
        public RestTemplate restTemplate() {
                return new RestTemplateBuilder().build();
        }

        @Autowired
        public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService);
        }

}