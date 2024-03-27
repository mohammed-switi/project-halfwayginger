package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Auth.UserNotAuthorizedException;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.User.UserService;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final JournalModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;

    public Journal convertJournalDtoToJournalEntity(Authentication authentication,
            JournalRequestDTO journalRequestDTO) {

        return Journal.builder()
                .content(journalRequestDTO.getContent())
                .visibility(journalRequestDTO.getVisibility())
                .publisher(getUserByPublisherId(jwtService.extractId(authentication)))
                .build();
    }

    public User getUserByPublisherId(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public User findUserById(Long userId) {
        return (userRepository.findById(userId))
                .orElseThrow(UserNotFoundException::new);

    }

    public EntityModel<Journal> findJournalById(Long journalId) {

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId));

        return assembler.toModel(journal);
    }

    // We Should Specify An Admin Authority To get All Journals
    public CollectionModel<EntityModel<Journal>> findAllJournals() {
        return CollectionModel.of(journalRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()));

    }

    public ResponseEntity<?> addContributor(Long journalId, Long contributorId) {

        User contributorUser = userRepository.findById(contributorId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with id: " + contributorId,
                        HttpStatus.NOT_FOUND));
        Journal journal = journalRepository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        if (journal.getContributors().stream().anyMatch(u -> u.getId() == contributorId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user is already a contributor");
        }

        journal.getContributors().add(contributorUser);
        journalRepository.save(journal);

        contributorUser.getContributs().add(journal);
        userRepository.save(contributorUser);

        EntityModel<Journal> entityModel = assembler.toModel(journal);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

    }

    public ResponseEntity<?> removeContributor(Long journalId,
            Long contributorId) {
        Journal journal = journalRepository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        User contributor = userRepository.findById(contributorId)
                .orElseThrow(
                        () -> new UserNotFoundException("Contributor is not found with id: " + contributorId,
                                HttpStatus.NOT_FOUND));
        if (journal.getContributors().stream().anyMatch(u -> u.getId() == contributorId)) {
            contributor.getContributs().remove(journal);
            journal.getContributors().remove(contributor);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user is already not a contributor");
        }
        userRepository.save(contributor);
        EntityModel<Journal> entityModel = assembler.toModel(journalRepository.save(journal));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

}
