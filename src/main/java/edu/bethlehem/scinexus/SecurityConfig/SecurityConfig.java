package edu.bethlehem.scinexus.Config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

import org.json.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.User;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final JournalRepository journalRepository;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(HttpRequestAuthorizer -> HttpRequestAuthorizer
                                                .requestMatchers("/api/v1/auth/**").permitAll()

                                                .requestMatchers(HttpMethod.GET, "/articles/{journalId}")
                                                .access(readJournals())
                                                .requestMatchers(HttpMethod.PUT, "/articles/{journalId}")
                                                .access(journalOwnerContributors())
                                                .requestMatchers(HttpMethod.PATCH, "/articles/{journalId}")
                                                .access(journalOwnerContributors())

                                                .requestMatchers(HttpMethod.DELETE, "/articles/{journalId}")
                                                .access(journalOwner())

                                                .requestMatchers("/journals/{journalId}/contributors/{contributorId}")
                                                .access(journalOwner())

                                                .anyRequest().authenticated())
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

        AuthorizationManager<RequestAuthorizationContext> readJournals() {
                return new AuthorizationManager<RequestAuthorizationContext>() {
                        @Override
                        public AuthorizationDecision check(Supplier<Authentication> authentication,
                                        RequestAuthorizationContext object) {
                                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                                Journal journal = journalRepository.findById(journalId)
                                                .orElseThrow(() -> new JournalNotFoundException(journalId,
                                                                HttpStatus.NOT_FOUND));
                                User user = (User) authentication.get().getPrincipal();
                                // check if the user is the publisher journal
                                if (journal.getVisibility().equals(Visibility.PUBLIC)) {
                                        return new AuthorizationDecision(true);
                                } else if (journal.getPublisher().getId().equals(user.getId())) {
                                        return new AuthorizationDecision(true);
                                }
                                Boolean isContributor = journal.getContributors().stream()
                                                .anyMatch(contributor -> contributor.getId().equals(user.getId()));
                                if (isContributor)
                                        return new AuthorizationDecision(true);

                                // add LINKS Visibility check

                                return new AuthorizationDecision(false);

                        }
                };
        }

        AuthorizationManager<RequestAuthorizationContext> journalOwner() {
                return new AuthorizationManager<RequestAuthorizationContext>() {
                        @Override
                        public AuthorizationDecision check(Supplier<Authentication> authentication,
                                        RequestAuthorizationContext object) {
                                System.out.println("journalOwnerFIRST");
                                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                                Journal journal = journalRepository.findById(journalId)
                                                .orElseThrow(() -> new JournalNotFoundException(journalId,
                                                                HttpStatus.NOT_FOUND));
                                User user = (User) authentication.get().getPrincipal();
                                // check if the user is the publisher journal

                                if (journal.getPublisher().getId().equals(user.getId())) {
                                        return new AuthorizationDecision(true);
                                }

                                System.out.println("journalOwnerFLSE");
                                return new AuthorizationDecision(false);

                        }
                };
        }

        AuthorizationManager<RequestAuthorizationContext> journalOwnerContributors() {
                return new AuthorizationManager<RequestAuthorizationContext>() {
                        @Override
                        public AuthorizationDecision check(Supplier<Authentication> authentication,
                                        RequestAuthorizationContext object) {
                                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                                Journal journal = journalRepository.findById(journalId)
                                                .orElseThrow(() -> new JournalNotFoundException(journalId,
                                                                HttpStatus.NOT_FOUND));
                                User user = (User) authentication.get().getPrincipal();
                                // check if the user is the publisher journal
                                if (journal.getPublisher().getId().equals(user.getId())) {
                                        return new AuthorizationDecision(true);
                                }
                                Boolean isContributor = journal.getContributors().stream()
                                                .anyMatch(contributor -> contributor.getId().equals(user.getId()));
                                if (isContributor)
                                        return new AuthorizationDecision(true);

                                return new AuthorizationDecision(false);

                        }
                };
        }

}