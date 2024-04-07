package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import java.io.IOException;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.File.FileStorageService;
import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.JPARepository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journals")
public class JournalController {

    private final JournalService service;

    @GetMapping("/{journalId}")
    public ResponseEntity<EntityModel<Journal>> one(@PathVariable Long journalId) {

        return ResponseEntity.ok(service.findJournalById(journalId));
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<Journal>>> all() {

        return ResponseEntity.ok(service.findAllJournals());
    }

    @PatchMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> addContributorNew(@PathVariable Long journalId, @PathVariable Long contributorId) {

        EntityModel<Journal> entityModel = service.addContributor(journalId, contributorId);
        return new ResponseEntity<>(entityModel,HttpStatus.CREATED);
     //   return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> removeContributorNew(@PathVariable Long journalId, @PathVariable Long contributorId) {

        service.removeContributor(journalId, contributorId);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("{journalId}/media")
    public ResponseEntity<?> attachMedia(@PathVariable Long journalId, @RequestBody MediaIdDTO mediaIds) {
        return ResponseEntity.ok(service.attachMedia(journalId, mediaIds));
    }

    @GetMapping("{journalId}/interactions")
    public ResponseEntity<?> getJournalInteractions(@PathVariable Long journalId) {
        return ResponseEntity.ok(service.getJournalInteractions(journalId));
    }

    @GetMapping("{journalId}/opinions")
    public ResponseEntity<?> getJournalOpinions(@PathVariable Long journalId) {
        return ResponseEntity.ok(service.getJournalOpinions(journalId));
    }

    @DeleteMapping("{journalId}/media")
    public ResponseEntity<?> deattachMedia(@PathVariable Long journalId, @RequestBody MediaIdDTO mediaIds) {
        return ResponseEntity.ok(service.deattachMedia(journalId, mediaIds));
    }

}
