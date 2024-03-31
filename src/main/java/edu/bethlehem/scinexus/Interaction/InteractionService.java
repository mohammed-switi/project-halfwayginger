package edu.bethlehem.scinexus.Interaction;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionNotFoundException;
import edu.bethlehem.scinexus.Interaction.InteractionRepository;
import edu.bethlehem.scinexus.Interaction.InteractionRequestDTO;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Opinion.OpinionRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteractionService {

        private final JwtService jwtService;
        private final UserRepository userRepository;
        private final InteractionRepository interactionRepository;
        private final OpinionRepository opinionRepository;
        private final JournalRepository journalRepository;
        private final InteractionModelAssembler assembler;
        Logger logger = LoggerFactory.getLogger(DataLoader.class);

        public User getUserId(long id) {
                logger.trace("Getting User by ID");
                return userRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

        }

        public EntityModel<Interaction> findInteractionById(Long InteractionId) {
                logger.trace("Finding Interaction by ID");
                Interaction interaction = interactionRepository.findById(
                                InteractionId)
                                .orElseThrow(() -> new InteractionNotFoundException(InteractionId,
                                                HttpStatus.NOT_FOUND));

                return assembler.toModel(interaction);
        }

        // We Should Specify An Admin Authority To get All Interactions
        public CollectionModel<EntityModel<Interaction>> findAllInteractions() {
                logger.trace("Finding All Interactions");
                List<EntityModel<Interaction>> interactions = interactionRepository
                                .findAll()
                                .stream()
                                .map(assembler::toModel)
                                .collect(Collectors.toList());
                return CollectionModel.of(interactions,
                                linkTo(methodOn(InteractionController.class).all()).withSelfRel());
        }

        public Interaction saveInteraction(Interaction interaction) {
                logger.trace("Saving Interaction");
                return interactionRepository.save(interaction);
        }

        public EntityModel<Interaction> createInteraction(InteractionType type, Authentication authentication) {
                logger.trace("Creating Interaction");
                User user = getUserId(jwtService.extractId(authentication));
                Interaction interaction = new Interaction(type, user);
                return assembler.toModel(saveInteraction(interaction));
        }

        public EntityModel<Interaction> updateInteraction(Long interactionId,
                        InteractionRequestDTO newInteractionRequestDTO) {
                logger.trace("Updating Interaction");
                return interactionRepository.findById(
                                interactionId)
                                .map(interaction -> {
                                        interaction.setType(newInteractionRequestDTO.getType());
                                        return assembler.toModel(interactionRepository.save(interaction));
                                })
                                .orElseThrow(() -> new InteractionNotFoundException(
                                                interactionId, HttpStatus.UNPROCESSABLE_ENTITY));
        }

        public EntityModel<Interaction> addOpinionInteraction(
                        Long opinionId,
                        InteractionRequestDTO interactionDTO,
                        Authentication authentication) {
                logger.trace("Adding Opinion Interaction");

                User user = userRepository.findById(((User) authentication.getPrincipal()).getId())
                                .orElseThrow(
                                                () -> new UserNotFoundException(
                                                                "User is not found with username: "
                                                                                + authentication.getName(),
                                                                HttpStatus.NOT_FOUND));

                Opinion opinion = opinionRepository.findById(
                                opinionId)
                                .orElseThrow(() -> new JournalNotFoundException(opinionId, HttpStatus.NOT_FOUND));
                Interaction interaction = new Interaction(interactionDTO.getType(), user);
                interaction.setOpinion(opinion);

                interaction = interactionRepository.save(interaction);

                opinion.addInteraction();
                opinionRepository.save(opinion);

                return assembler.toModel(interaction);

        }

        public EntityModel<Interaction> addJournalInteraction(
                        Long journalId,
                        InteractionRequestDTO interactionDTO,
                        Authentication authentication) {
                logger.trace("Adding Journal Interaction");

                User user = userRepository.findById(((User) authentication.getPrincipal()).getId())
                                .orElseThrow(
                                                () -> new UserNotFoundException(
                                                                "User is not found with username: "
                                                                                + authentication.getName(),
                                                                HttpStatus.NOT_FOUND));

                Journal journal = journalRepository.findById(
                                journalId)
                                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
                Interaction interaction = new Interaction(interactionDTO.getType(), user);

                interaction.setJournal(journal);
                interaction.setInteractorUser(user);

                journal.addInteraction();
                journalRepository.save(journal);
                return assembler.toModel(interactionRepository.save(interaction));

        }

        public void deleteInteraction(Long interactionId, Authentication authentication) {
                logger.trace("Deleting Interaction");
                User user = userRepository.findById(((User) authentication.getPrincipal()).getId())
                                .orElseThrow(
                                                () -> new UserNotFoundException(
                                                                "User is not found with username: "
                                                                                + authentication.getName(),
                                                                HttpStatus.NOT_FOUND));
                Interaction interaction = interactionRepository.findByIdAndInteractorUser(interactionId, user);
                if (interaction == null)

                        throw new InteractionNotFoundException("Interaction with id: " + interactionId
                                        + " for user with Id: " + user.getId() + " is not Found", HttpStatus.NOT_FOUND);

                if (interaction.getJournal() != null)
                        interaction.getJournal().removeInteraction();
                if (interaction.getOpinion() != null)
                        interaction.getOpinion().removeInteraction();
                interactionRepository.delete(interaction);
        }

}
