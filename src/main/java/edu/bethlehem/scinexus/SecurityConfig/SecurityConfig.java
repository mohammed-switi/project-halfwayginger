package edu.bethlehem.scinexus.SecurityConfig;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.JwtBearerOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig  {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final AuthorizationManager authorizationManager;
       // private final CustomJwtDecoder customJwtDecoder;
     //   private final WebClient userClientInfo;

        private final JwtDecoder jwtDecoder;
        private final UserService userDetailsService;



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity    //.cors(Customizer.withDefaults())
                            //   .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
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

                                                .requestMatchers(HttpMethod.GET, "/articles/{journalId}")
                                                .access(authorizationManager.readJournals())

                                                .requestMatchers(HttpMethod.PUT, "/articles/{journalId}")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.PATCH, "/articles/{journalId}")
                                                .access(authorizationManager.journalOwnerContributors())

                                                .requestMatchers(HttpMethod.DELETE, "/articles/{journalId}")
                                                .access(authorizationManager.journalOwner())

                                                .requestMatchers(HttpMethod.POST, "/journals/**")
                                                .access(authorizationManager.journalOwner())

                                                .requestMatchers(HttpMethod.PATCH, "/journals/{journalId}/contributors/{contributorId}")
                                                .access(authorizationManager.journalOwnerNew())

                                                .requestMatchers(HttpMethod.DELETE, "/journals/{journalId}/contributors/{contributorId}")
                                                .access(authorizationManager.journalOwnerNew())

                                                .requestMatchers("/api/v1/auth/login").permitAll()
                                                .anyRequest().authenticated())

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
                                                 oauth2LoginConfig.defaultSuccessUrl("/dashboard",true);
                                                   }
                                         )

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