package edu.bethlehem.scinexus.Interaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.apache.catalina.connector.Response;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("interactions")
public class InteractionController {

  private final InteractionRepository repository;
  private final UserRepository userRepository;
  private final JournalRepository journalRepository;
  private final OpinionRepository opinionRepository;
  private final InteractionModelAssembler assembler;
  private final InteractionService service;

  @GetMapping("/{interactionId}")
  EntityModel<Interaction> one(@PathVariable Long interactionId) {
    // TBC
    return service.findInteractionById(interactionId);
  }

  @GetMapping()
  CollectionModel<EntityModel<Interaction>> all() {
    // TBC
    return service.findAllInteractions();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateUserPartially(@PathVariable(value = "id") Long interactionId,
      @Valid @RequestBody InteractionRequestDTO newInteraction) {
    return ResponseEntity.ok().body(service.updateInteraction(interactionId, newInteraction));
  }

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteInteraction(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();

  }

  @PostMapping("/opinion/{opinionId}")
  public ResponseEntity<?> addOpinionInteraction(
      @PathVariable(value = "journalId") Long opinionId,
      @RequestBody InteractionRequestDTO interaction,
      Authentication authentication) {

    return ResponseEntity.ok().body(service.addOpinionInteraction(opinionId, interaction, authentication));
  }

  @PostMapping("/journal/{journalId}")
  public ResponseEntity<?> addJournalInteraction(
      @PathVariable(value = "journalId") Long journalId,
      @RequestBody InteractionRequestDTO interaction,
      Authentication authentication) {
    return ResponseEntity.ok(service.addJournalInteraction(journalId, interaction, authentication));
  }
}
