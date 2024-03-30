package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaNotFoundException;
import edu.bethlehem.scinexus.Media.MediaRepository;
import edu.bethlehem.scinexus.User.UserService;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private final MediaRepository mediaRepository;
    private final JournalModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthorizationManager authorizationManager;

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

    @Transactional
    public EntityModel<Journal> addContributor(Long journalId, Long contributorId) {

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

    public void removeContributor(Long journalId, Long contributorId) {

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

    public EntityModel<Journal> attachMedia(Long journalId, MediaIdDTO mediaIds) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        for (Long mediaId : mediaIds.getMediaIds()) {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND));
            journal.getMedias().add(media);
            media.setOwnerJournal(journal);
        }
        mediaRepository.saveAll(journal.getMedias());
        return assembler.toModel(journalRepository.save(journal));
    }

    public EntityModel<Journal> deattachMedia(Long journalId, MediaIdDTO mediaIds) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        for (Long mediaId : mediaIds.getMediaIds()) {

            journal.getMedias().stream().filter(m -> Objects.equals(m.getId(), mediaId)).anyMatch(m -> {
                journal.getMedias().remove(m);
                mediaRepository.delete(m);
                return true;
            });

        }
        mediaRepository.saveAll(journal.getMedias());
        return assembler.toModel(journalRepository.save(journal));
    }

}
