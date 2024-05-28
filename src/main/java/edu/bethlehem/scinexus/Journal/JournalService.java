package edu.bethlehem.scinexus.Journal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.bethlehem.scinexus.Authorization.AuthorizationManager;
import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionModelAssembler;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaNotFoundException;
import edu.bethlehem.scinexus.Notification.NotificationService;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Opinion.OpinionModelAssembler;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Data
public class JournalService {

    private final OpinionRepository opinionRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final JournalModelAssembler assembler;
    private final InteractionModelAssembler interactionAssembler;
    private final OpinionModelAssembler opinionAssembler;
    private final JwtService jwtService;
    private final NotificationService notificationService;
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
    public EntityModel<Journal> addContributor(Long journalId, Long contributorId) {
        logger.trace("Adding Contributor");
        User contributorUser = findUserById(contributorId);

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
        notificationService.notifyUser(contributorUser,
                "You have been added to contribute to this journal " + journal.getId(), linkTo(methodOn(
                        JournalController.class).one(
                                journal.getId())));
        // Return response with created journal entity
        EntityModel<Journal> entityModel = assembler.toModel(journal);
        return entityModel;
    }

    @Transactional
    public void removeContributor(Long journalId, Long contributorId) {
        logger.trace("Removing Contributor");
        User contributorUser = findUserById(contributorId);

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        if (!journal.getContributors().contains(contributorUser))
            throw new ContributionException("User is Already Not A Contributor");

        // Add contributor to the journal and save
        journal.getContributors().remove(contributorUser);
        journal = journalRepository.save(journal);

        // Add journal to the contributor's contributed journals and save
        contributorUser.getContributedJournals().remove(journal);
        userRepository.save(contributorUser);

    }

    public EntityModel<Journal> attachMedia(Long journalId, MediaIdDTO mediaIds) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        for (Long mediaId : mediaIds.getMediaIds()) {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND));
            if (media.getOwnerJournal() != null)
                throw new MediaNotFoundException("media is already attached to a journal", HttpStatus.CONFLICT);
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

    public CollectionModel<EntityModel<Interaction>> getJournalInteractions(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        List<EntityModel<Interaction>> interactions = journal.getInteractions()
                .stream()
                .map(interactionAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(interactions);
    }

    // public CollectionModel<EntityModel<Opinion>> getJournalOpinions(Long
    // journalId) {
    // Journal journal = journalRepository.findById(journalId)
    // .orElseThrow(() -> new JournalNotFoundException(journalId,
    // HttpStatus.NOT_FOUND));
    // List<EntityModel<Opinion>> opinions = journal.getOpinions()
    // .stream()
    // .map(opinionAssembler::toModel)
    // .collect(Collectors.toList());
    // return CollectionModel.of(opinions);
    // }
    public CollectionModel<EntityModel<Opinion>> getJournalOpinions(Long journalId) {

        List<EntityModel<Opinion>> opinions = opinionRepository.findByJournalId(journalId)
                .parallelStream()
                .map(opinionAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(opinions);
    }


}
