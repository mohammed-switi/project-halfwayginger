package edu.bethlehem.scinexus.Journal;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionRepository;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journals")
public class JournalController {

    private final JournalRepository repository;
    private final JournalModelAssembler assembler;
    private final UserRepository userRepository;
    private final InteractionRepository interactionRepository;

    @GetMapping("/{journalId}")
    EntityModel<Journal> one(@PathVariable Long journalId) {

        Journal journal = repository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        return assembler.toModel(journal);
    }

    @GetMapping()
    CollectionModel<EntityModel<Journal>> all() {
        List<EntityModel<Journal>> journals = repository.findAll().stream().map(journal -> assembler.toModel(journal))
                .collect(Collectors.toList());

        return CollectionModel.of(journals, linkTo(methodOn(JournalController.class).all()).withSelfRel());
    }

    @PostMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> addContributor(
            @PathVariable(value = "journalId") Long journalId,
            @PathVariable Long contributorId) {

        User contributorUser = userRepository.findById(contributorId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with id: " + contributorId,
                        HttpStatus.NOT_FOUND));
        Journal journal = repository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        if (journal.getContributors().stream().anyMatch(u -> u.getId() == contributorId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user is already a contributor");
        }

        journal.getContributors().add(contributorUser);
        repository.save(journal);

        contributorUser.getContributs().add(journal);
        userRepository.save(contributorUser);

        EntityModel<Journal> entityModel = assembler.toModel(journal);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> removeContributor(@PathVariable(value = "journalId") Long journalId,
            @PathVariable Long contributorId) {

        Journal journal = repository.findById(
                journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));

        User contributor = userRepository.findById(contributorId)
                .orElseThrow(() -> new UserNotFoundException("Contributor is not found with id: " + contributorId,
                        HttpStatus.NOT_FOUND));
        if (journal.getContributors().stream().anyMatch(u -> u.getId() == contributorId)) {
            contributor.getContributs().remove(journal);
            journal.getContributors().remove(contributor);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user is already not a contributor");
        }
        userRepository.save(contributor);
        EntityModel<Journal> entityModel = assembler.toModel(repository.save(journal));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

}
