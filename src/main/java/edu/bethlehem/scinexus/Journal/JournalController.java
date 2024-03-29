package edu.bethlehem.scinexus.Journal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import edu.bethlehem.scinexus.Interaction.InteractionRepository;
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

    @PostMapping()
    public ResponseEntity<?> addContributor(@RequestBody @Valid ContributionDTO contributionDTO) {

        EntityModel<Journal> entityModel = service.addContributor(contributionDTO);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PatchMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> addContributorNew(@PathVariable Long journalId, @PathVariable Long contributorId) {

        EntityModel<Journal> entityModel = service.addContributor(journalId, contributorId);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> removeContributorNew(@PathVariable Long journalId, @PathVariable Long contributorId) {

        service.removeContributor(journalId, contributorId);
        return ResponseEntity.noContent().build();
        // return
        // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping()
    public ResponseEntity<?> removeContributor(@RequestBody @Valid ContributionDTO contributionDTO) {
        service.removeContributor(contributionDTO);
        return ResponseEntity.noContent().build();
    }

}
