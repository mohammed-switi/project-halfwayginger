package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;
import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.User.UserService;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuthorizationManager authorizationManager;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public Journal convertJournalDtoToJournalEntity(Authentication authentication,
            JournalRequestDTO journalRequestDTO) {

        return Journal.builder()
                .content(journalRequestDTO.getContent())
                .visibility(journalRequestDTO.getVisibility())
                .publisher(getUserByPublisherId(jwtService.extractId(authentication)))
                .build();
    }

    public User getUserByPublisherId(long id) {
        logger.trace("Getting User by Publisher ID");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public User findUserById(Long userId) {
        logger.trace("Finding User by ID");
        return (userRepository.findById(userId))
                .orElseThrow(UserNotFoundException::new);

    }

    public EntityModel<Journal> findJournalById(Long journalId) {
        logger.trace("Finding Journal by ID");
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId));

        return assembler.toModel(journal);
    }

    // We Should Specify An Admin Authority To get All Journals
    public CollectionModel<EntityModel<Journal>> findAllJournals() {
        logger.trace("Finding All Journals");
        return CollectionModel.of(journalRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()));

    }

    @Transactional
    public EntityModel<Journal> addContributor(ContributionDTO contributionDTO) {
        logger.trace("Adding Contributor");
        Long contributorId = contributionDTO.getUserId();
        Long journalId = contributionDTO.getJournalId();

        User contributorUser = userRepository.findById(contributorId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with id: " + contributorId,
                        HttpStatus.NOT_FOUND));

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        if (authorizationManager.isJournalOwner(journalId, contributorUser))
            throw new ContributionException("Journal Owner Can't be A Contributor");

        if (journal.getContributors().contains(contributorUser))
            throw new ContributionException("User is Already A Contributor");

        // Add contributor to the journal and save
        journal.getContributors().add(contributorUser);
        journalRepository.save(journal);

        // Add journal to the contributor's contributed journals and save
        contributorUser.getContributedJournals().add(journal);
        userRepository.save(contributorUser);

        // Return response with created journal entity
        EntityModel<Journal> entityModel = assembler.toModel(journal);
        return entityModel;
    }

    @Transactional
    public EntityModel<Journal> addContributor(Long journalId, Long contributorId) {
        logger.trace("Adding Contributor");
        User contributorUser = userRepository.findById(contributorId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with id: " + contributorId,
                        HttpStatus.NOT_FOUND));

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        if (authorizationManager.isJournalOwner(journalId, contributorUser))
            throw new ContributionException("Journal Owner Can't be A Contributor");

        if (journal.getContributors().contains(contributorUser))
            throw new ContributionException("User is Already A Contributor");

        // Add contributor to the journal and save
        journal.getContributors().add(contributorUser);
        journalRepository.save(journal);

        // Add journal to the contributor's contributed journals and save
        contributorUser.getContributedJournals().add(journal);
        userRepository.save(contributorUser);

        // Return response with created journal entity
        EntityModel<Journal> entityModel = assembler.toModel(journal);
        return entityModel;
    }

    public void removeContributor(ContributionDTO contributionDTO) {
        logger.trace("Removing Contributor");
        Long journalId = contributionDTO.getJournalId();
        Long contributorId = contributionDTO.getUserId();

        Journal journal = journalRepository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        User contributor = userRepository.findById(contributorId)
                .orElseThrow(
                        () -> new UserNotFoundException(contributorId));
        if (journal.getContributors().contains(contributor)) {
            contributor.getContributedJournals().remove(journal);
            journal.getContributors().remove(contributor);
        } else {
            throw new ContributionException("The user is already not a contributor");
        }
        userRepository.save(contributor);

    }

    public void removeContributor(Long journalId, Long contributorId) {
        logger.trace("Removing Contributor");
        Journal journal = journalRepository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        User contributor = userRepository.findById(contributorId)
                .orElseThrow(
                        () -> new UserNotFoundException(contributorId));
        if (journal.getContributors().contains(contributor)) {
            contributor.getContributedJournals().remove(journal);
            journal.getContributors().remove(contributor);
        } else {
            throw new ContributionException("The user is already not a contributor");
        }
        userRepository.save(contributor);

    }

}
