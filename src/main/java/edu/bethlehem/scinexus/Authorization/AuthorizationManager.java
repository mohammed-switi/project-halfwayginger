package edu.bethlehem.scinexus.Authorization;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bethlehem.scinexus.Auth.UserNotAuthorizedException;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaRepository;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationRepository;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Opinion.OpinionNotFoundException;
import edu.bethlehem.scinexus.Opinion.OpinionRepository;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.UserLinks.UserLinksRepository;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

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
    private final EntityManager entityManager;

    // Enhanced Version Of Code
    public boolean isJournalOwner(Long journalId, User user) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        return journal.getPublisher().getId().equals(user.getId());
    }
    //
    // public
    // org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext>
    // readJournals() {
    // return (authentication, object) -> {
    // Long journalId = Long.parseLong(object.getVariables().get("journalId"));
    // User user = (User) authentication.get().getPrincipal();
    //
    // Journal journal = journalRepository.findById(journalId).orElse(null);
    // if (journal == null) {
    // throw new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND);
    // }
    //
    // if (journal.getVisibility().equals(Visibility.PUBLIC)) {
    // return new AuthorizationDecision(true);
    // } else if (isJournalOwner(journalId, user)) {
    // return new AuthorizationDecision(true);
    // } else if (journal.getVisibility().equals(Visibility.LINKS)) {
    // return checkLinkedUser(user, journal.getPublisher().getId());
    // }
    // return new AuthorizationDecision(false);
    // };
    // }
    //
    // private AuthorizationDecision checkLinkedUser(User user, Long publisherId) {
    // Long count = (Long) entityManager.createQuery(
    // "SELECT COUNT(u) FROM User u JOIN u.links l " +
    // "WHERE u.id = :userId1 AND l.id = :userId2",
    // Long.class)
    // .setParameter("userId1", user.getId())
    // .setParameter("userId2", publisherId)
    // .getSingleResult();
    //
    // return new AuthorizationDecision(count > 0);
    // }
    //
    // public
    // org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext>
    // journalOwner() {
    // return (authentication, object) -> {
    // Long journalId = Long.parseLong(object.getVariables().get("journalId"));
    // User user = (User) authentication.get().getPrincipal();
    // System.out.println(isJournalOwner(journalId,user) +
    // "hello++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    // return new AuthorizationDecision(isJournalOwner(journalId, user));
    // };
    // }
    //
    // public
    // org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext>
    // journalOwnerContributors() {
    // return (authentication, object) -> {
    // Long journalId = Long.parseLong(object.getVariables().get("journalId"));
    // User user = (User) authentication.get().getPrincipal();
    //
    // if (isJournalOwner(journalId, user)) {
    // return new AuthorizationDecision(true);
    // }
    //
    // Journal journal = journalRepository.findById(journalId).orElse(null);
    // if (journal == null) {
    // throw new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND);
    // }
    //
    // Boolean isContributor = journal.getContributors().stream()
    // .anyMatch(contributor -> contributor.getId().equals(user.getId()));
    // return new AuthorizationDecision(isContributor);
    // };
    // }
    // }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> readJournals() {
        return (authentication, object) -> {
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

            if (journal.getVisibility().equals(Visibility.LINKS)) {
                Long journalPublisherId = journal.getPublisher().getId();

                if (ulService.areTheyLinked(user.getId(), journalPublisherId))
                    return new AuthorizationDecision(true);

            }

            return new AuthorizationDecision(false);
        };
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> admin() {
        return (authentication, object) -> {
            User admin = userRepository.findById(((User) authentication.get().getPrincipal()).getId())
                    .orElseThrow(() -> new UserNotFoundException());
            if (admin.getRole() == Role.ADMIN)
                return new AuthorizationDecision(true);
            return new AuthorizationDecision(false);
        };
    }

    public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> userHimSelfAndAdmin() {
        return (authentication, context) -> {

            User admin = userRepository.findById(((User) authentication.get().getPrincipal()).getId())
                    .orElseThrow(() -> new UserNotFoundException());
            if (admin.getRole() == Role.ADMIN)
                return new AuthorizationDecision(true);

            Long academicId = Long.parseLong(context.getVariables().get("userId"));

            if (((User) authentication.get().getPrincipal()).getId() == academicId)
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
            User user = (User) authentication.get().getPrincipal();
            // check if the user is the publisher journal

            if (journal.getPublisher().getId().equals(user.getId())) {

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
            User user = (User) authentication.get().getPrincipal();
            // check if the user is the publisher journal

            if (journal.getPublisher().getId().equals(user.getId())) {
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
            User user = (User) authentication.get().getPrincipal();

            if (interaction.getInteractorUser().getId().equals(user.getId()))
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
            User user = (User) authentication.get().getPrincipal();

            if (media.getOwner().getId().equals(user.getId()))
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
            User user = (User) authentication.get().getPrincipal();

            if (notification.getUser().getId().equals(user.getId()))
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
            User user = (User) authentication.get().getPrincipal();

            if (opinon.getOpinionOwner().getId().equals(user.getId()))
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

        };
    }

}
