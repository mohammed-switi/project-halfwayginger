package edu.bethlehem.scinexus.Interaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.JournalRepository;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Opinion.OpinionRepository;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InteractionController {

  private final InteractionRepository repository;
  private final UserRepository userRepository;
  private final JournalRepository journalRepository;
  private final OpinionRepository opinionRepository;
  private final InteractionModelAssembler assembler;

  @GetMapping("/interactions/{interactionId}")
  EntityModel<Interaction> one(@PathVariable Long interactionId) {

    Interaction interaction = repository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId, HttpStatus.NOT_FOUND));

    return assembler.toModel(interaction);
  }

  @GetMapping("/interactions")
  CollectionModel<EntityModel<Interaction>> all() {
    List<EntityModel<Interaction>> interactions = repository.findAll().stream()
        .map(interaction -> assembler.toModel(interaction))
        .collect(Collectors.toList());

    return CollectionModel.of(interactions, linkTo(methodOn(InteractionController.class).all()).withSelfRel());
  }

  @PatchMapping("/interactions/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long interactionId,
      @RequestBody Interaction newInteraction) {
    Interaction interaction = repository.findById(interactionId)
        .orElseThrow(() -> new InteractionNotFoundException(interactionId, HttpStatus.NOT_FOUND));

    if (newInteraction.getInteractionId() != null)
      interaction.setInteractionId(newInteraction.getInteractionId());
    if (newInteraction.getType() != null)
      interaction.setType(newInteraction.getType());

    EntityModel<Interaction> entityModel = assembler.toModel(repository.save(interaction));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @DeleteMapping("/interactions/{id}")
  ResponseEntity<?> deleteInteraction(@PathVariable Long id) {

    Interaction interaction = repository.findById(id)
        .orElseThrow(() -> new InteractionNotFoundException(id, HttpStatus.NOT_FOUND));

    repository.delete(interaction);

    return ResponseEntity.noContent().build();

  }

  @PostMapping("/opinion/{opinionId}")
  public ResponseEntity<?> addOpinionInteraction(
      @PathVariable(value = "journalId") Long opinionId,
      @RequestBody Interaction interaction,
      Authentication authentication) {

    User user = userRepository.findById(((User) authentication.getPrincipal()).getId())
        .orElseThrow(
            () -> new UserNotFoundException("User is not found with username: " + authentication.getName(),
                HttpStatus.NOT_FOUND));

    Opinion opinion = opinionRepository.findById(
        opinionId)
        .orElseThrow(() -> new JournalNotFoundException(opinionId, HttpStatus.NOT_FOUND));
    interaction.setOpinion(opinion);
    interaction.setInteractorUser(user);

    interaction = repository.save(interaction);

    opinion.getInteractions().add(interaction);

    userRepository.save(user);
    opinionRepository.save(opinion);
    EntityModel<Interaction> entityModel = assembler.toModel(interaction);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

  @PostMapping("/journal/{journalId}")
  public ResponseEntity<?> addJournalInteraction(
      @PathVariable(value = "journalId") Long journalId,
      @RequestBody Interaction interaction,
      Authentication authentication) {

    User user = userRepository.findById(((User) authentication.getPrincipal()).getId())
        .orElseThrow(
            () -> new UserNotFoundException("User is not found with username: " + authentication.getName(),
                HttpStatus.NOT_FOUND));

    Journal journal = journalRepository.findById(
        journalId)
        .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
    interaction.setJournal(journal);
    interaction.setInteractorUser(user);

    interaction = repository.save(interaction);

    journal.getInteractions().add(interaction);

    userRepository.save(user);
    journalRepository.save(journal);
    EntityModel<Interaction> entityModel = assembler.toModel(interaction);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }

}
