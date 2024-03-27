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
    private final JournalService service;

    @GetMapping("/{journalId}")
    EntityModel<Journal> one(@PathVariable Long journalId) {

        return service.findJournalById(journalId);
    }

    @GetMapping()
    CollectionModel<EntityModel<Journal>> all() {

        return service.findAllJournals();
    }

    @PostMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> addContributor(
            @PathVariable(value = "journalId") Long journalId,
            @PathVariable Long contributorId) {

        return service.addContributor(journalId, contributorId);
    }

    @DeleteMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> removeContributor(@PathVariable(value = "journalId") Long journalId,
            @PathVariable Long contributorId) {

        return service.removeContributor(journalId, contributorId);
    }

}
