package edu.bethlehem.scinexus.Authorization;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class AuthorizationManager {


        private final JournalRepository journalRepository;
        private final EntityManager entityManager;


        // Enhanced Version Of Code
        private boolean isJournalOwner(Long journalId, User user) {
            Journal journal = journalRepository.findById(journalId)
                    .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
            return journal.getPublisher().getId().equals(user.getId());
        }

        public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> readJournals() {
            return (authentication, object) -> {
                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                User user = (User) authentication.get().getPrincipal();

                Journal journal = journalRepository.findById(journalId).orElse(null);
                if (journal == null) {
                    throw new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND);
                }

                if (journal.getVisibility().equals(Visibility.PUBLIC)) {
                    return new AuthorizationDecision(true);
                } else if (isJournalOwner(journalId, user)) {
                    return new AuthorizationDecision(true);
                } else if (journal.getVisibility().equals(Visibility.LINKS)) {
                    return checkLinkedUser(user, journal.getPublisher().getId());
                }
                return new AuthorizationDecision(false);
            };
        }

        private AuthorizationDecision checkLinkedUser(User user, Long publisherId) {
            Long count = (Long) entityManager.createQuery(
                            "SELECT COUNT(u) FROM User u JOIN u.links l " +
                                    "WHERE u.id = :userId1 AND l.id = :userId2",
                            Long.class)
                    .setParameter("userId1", user.getId())
                    .setParameter("userId2", publisherId)
                    .getSingleResult();

            return new AuthorizationDecision(count > 0);
        }

        public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwner() {
            return (authentication, object) -> {
                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                User user = (User) authentication.get().getPrincipal();
                return new AuthorizationDecision(isJournalOwner(journalId, user));
            };
        }

        public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwnerContributors() {
            return (authentication, object) -> {
                Long journalId = Long.parseLong(object.getVariables().get("journalId"));
                User user = (User) authentication.get().getPrincipal();

                if (isJournalOwner(journalId, user)) {
                    return new AuthorizationDecision(true);
                }

                Journal journal = journalRepository.findById(journalId).orElse(null);
                if (journal == null) {
                    throw new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND);
                }

                Boolean isContributor = journal.getContributors().stream()
                        .anyMatch(contributor -> contributor.getId().equals(user.getId()));
                return new AuthorizationDecision(isContributor);
            };
        }
    }

    // This Is Old Code :
//    private final JournalRepository journalRepository;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//   public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> readJournals() {
//        return (authentication, object) -> {
//            Long journalId = Long.parseLong(object.getVariables().get("journalId"));
//            Journal journal = journalRepository.findById(journalId)
//                    .orElseThrow(() -> new JournalNotFoundException(journalId,
//                            HttpStatus.NOT_FOUND));
//            User user = (User) authentication.get().getPrincipal();
//            // check if the user is the publisher journal
//            if (journal.getVisibility().equals(Visibility.PUBLIC)) {
//                return new AuthorizationDecision(true);
//            } else if (journal.getPublisher().getId().equals(user.getId())) {
//                return new AuthorizationDecision(true);
//            }
//            Boolean isContributor = journal.getContributors().stream()
//                    .anyMatch(contributor -> contributor.getId().equals(user.getId()));
//            if (isContributor)
//                return new AuthorizationDecision(true);
//
//            // add LINKS Visibility check
//            if (journal.getVisibility().equals(Visibility.LINKS)) {
//                Long count = entityManager.createQuery(
//                                "SELECT COUNT(u) FROM User u JOIN u.links l " +
//                                        "WHERE u.id = :userId1 AND l.id = :userId2",
//                                Long.class)
//                        .setParameter("userId1", user.getId())
//                        .setParameter("userId2", journal.getPublisher().getId())
//                        .getSingleResult();
//
//                if (count > 0)
//                    return new AuthorizationDecision(true);
//            }
//            return new AuthorizationDecision(false);
//
//        };
//    }
//
//  public org.springframework.security.authorization.AuthorizationManager<RequestAuthorizationContext> journalOwner() {
//        return (authentication, object) -> {
//            System.out.println("journalOwnerFIRST");
//            Long journalId = Long.parseLong(object.getVariables().get("journalId"));
//            Journal journal = journalRepository.findById(journalId)
//                    .orElseThrow(() -> new JournalNotFoundException(journalId,
//                            HttpStatus.NOT_FOUND));
//            User user = (User) authentication.get().getPrincipal();
//            // check if the user is the publisher journal
//
//            if (journal.getPublisher().getId().equals(user.getId())) {
//                return new AuthorizationDecision(true);
//            }
//
//            return new AuthorizationDecision(false);
//
//        };
//    }
//
//   public org.springframework.security.authorization.
//           AuthorizationManager<RequestAuthorizationContext> journalOwnerContributors() {
//        return (authentication, object) -> {
//            Long journalId = Long.parseLong(object.getVariables().get("journalId"));
//            Journal journal = journalRepository.findById(journalId)
//                    .orElseThrow(() -> new JournalNotFoundException(journalId,
//                            HttpStatus.NOT_FOUND));
//            User user = (User) authentication.get().getPrincipal();
//            // check if the user is the publisher journal
//            if (journal.getPublisher().getId().equals(user.getId())) {
//                return new AuthorizationDecision(true);
//            }
//            Boolean isContributor = journal.getContributors().stream()
//                    .anyMatch(contributor -> contributor.getId().equals(user.getId()));
//            if (isContributor)
//                return new AuthorizationDecision(true);
//
//            return new AuthorizationDecision(false);
//
//        };
//    }


