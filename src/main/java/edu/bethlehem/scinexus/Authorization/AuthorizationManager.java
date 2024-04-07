package edu.bethlehem.scinexus.Authorization;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bethlehem.scinexus.Auth.UserNotAuthorizedException;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.JPARepository.NotificationRepository;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Opinion.OpinionNotFoundException;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.UserLinksRepository;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthorizationManager {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final OpinionRepository opinionRepository;
    private final UserLinksRepository userLinksRepository;
    private final InteractionRepository interactionRepository;
    private final MediaRepository mediaRepository;
    private final UserLinksService ulService;
    private final JwtService jwtService;
    private final EntityManager entityManager;

    // Enhanced Version Of Code
    public boolean isJournalOwner(Long journalId, User user) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        return journal.getPublisher().getId().equals(user.getId());
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> readJournals() {
        return (authentication, object) -> {
            Long journalId = Long.parseLong(object.getVariables().get("journalId"));
            Journal journal = journalRepository.findById(journalId)
                    .orElseThrow(() -> new JournalNotFoundException(journalId,
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());
            // check if the user is the publisher journal
            if (journal.getVisibility().equals(Visibility.PUBLIC)) {
                return new AuthorizationDecision(true);
            } else if (journal.getPublisher().getId().equals(userId)) {
                return new AuthorizationDecision(true);
            }
            Boolean isContributor = journal.getContributors().stream()
                    .anyMatch(contributor -> contributor.getId().equals(userId));
            if (isContributor)
                return new AuthorizationDecision(true);

            if (journal.getVisibility().equals(Visibility.LINKS)) {
                Long journalPublisherId = journal.getPublisher().getId();

                if (ulService.areTheyLinked(userId, journalPublisherId))
                    return new AuthorizationDecision(true);

            }

            return new AuthorizationDecision(false);
        };
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> admin() {
        return (authentication, object) -> {
            User admin = jwtService.getUser(authentication.get());
            if (admin.getRole() == Role.ADMIN)
                return new AuthorizationDecision(true);
            return new AuthorizationDecision(false);
        };
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> userHimSelfAndAdmin() {
        return (authentication, context) -> {

            User admin = jwtService.getUser(authentication.get());
            if (admin.getRole() == Role.ADMIN)
                return new AuthorizationDecision(true);

            Long academicId = Long.parseLong(context.getVariables().get("userId"));

            if (jwtService.extractId(authentication.get()) == academicId)
                return new AuthorizationDecision(true);
            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwner() {
        return (authentication, context) -> {

            Long journalId = Long.parseLong(context.getVariables().get("journalId"));

            Journal journal = journalRepository.findById(
                    journalId)
                    .orElseThrow(() -> new JournalNotFoundException(String.valueOf(
                            journalId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());
            // check if the user is the publisher journal

            if (journal.getPublisher().getId().equals(userId)) {

                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwnerNew() {
        return (authentication, context) -> {
            Long journalId = Long.parseLong(context.getVariables().get("journalId"));
            Journal journal = journalRepository.findById(
                    journalId)
                    .orElseThrow(() -> new JournalNotFoundException(String.valueOf(
                            journalId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());
            // check if the user is the publisher journal

            if (journal.getPublisher().getId().equals(userId)) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> interactionOwner() {
        return (authentication, context) -> {
            Long interactionId = Long.parseLong(context.getVariables().get("interactionId"));
            Interaction interaction = interactionRepository.findById(
                    interactionId)
                    .orElseThrow(() -> new JournalNotFoundException(String.valueOf(
                            interactionId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());

            if (interaction.getInteractorUser().getId().equals(userId))
                return new AuthorizationDecision(true);

            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> mediaOwner() {
        return (authentication, context) -> {
            Long mediaId = Long.parseLong(context.getVariables().get("mediaId"));
            Media media = mediaRepository.findById(
                    mediaId)
                    .orElseThrow(() -> new JournalNotFoundException(String.valueOf(
                            mediaId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());

            if (media.getOwner().getId().equals(userId))
                return new AuthorizationDecision(true);

            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> notificationOwner() {
        return (authentication, context) -> {
            Long notificaitionId = Long.parseLong(context.getVariables().get("notificaitionId"));
            Notification notification = notificationRepository.findById(
                    notificaitionId)
                    .orElseThrow(() -> new JournalNotFoundException(String.valueOf(
                            notificaitionId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());

            if (notification.getUser().getId().equals(userId))
                return new AuthorizationDecision(true);

            return new AuthorizationDecision(false);

        };
    }

    @Bean
    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> opinionOwner() {
        return (authentication, context) -> {
            Long opinionId = Long.parseLong(context.getVariables().get("opinionId"));
            Opinion opinon = opinionRepository.findById(
                    opinionId)
                    .orElseThrow(() -> new OpinionNotFoundException(String.valueOf(
                            opinionId),
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());

            if (opinon.getOpinionOwner().getId().equals(userId))
                return new AuthorizationDecision(true);

            return new AuthorizationDecision(false);

        };
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwnerContributors() {
        return (authentication, object) -> {
            Long journalId = Long.parseLong(object.getVariables().get("journalId"));
            Journal journal = journalRepository.findById(journalId)
                    .orElseThrow(() -> new JournalNotFoundException(journalId,
                            HttpStatus.NOT_FOUND));
            Long userId = jwtService.extractId(authentication.get());
            // check if the user is the publisher journal
            if (journal.getPublisher().getId().equals(userId)) {
                return new AuthorizationDecision(true);
            }
            Boolean isContributor = journal.getContributors().stream()
                    .anyMatch(contributor -> contributor.getId().equals(userId));
            if (isContributor)
                return new AuthorizationDecision(true);

            return new AuthorizationDecision(false);

        };
    }

}
