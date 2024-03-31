package edu.bethlehem.scinexus.Journal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;

import java.io.IOException;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.File.FileStorageService;
import edu.bethlehem.scinexus.Interaction.InteractionRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.User.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journals")
public class JournalController {

    private final JournalService service;

    @GetMapping("/{journalId}")
    EntityModel<Journal> one(@PathVariable Long journalId) {

        return service.findJournalById(journalId);
    }

    @GetMapping()
    CollectionModel<EntityModel<Journal>> all() {

        return service.findAllJournals();
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

    }

    @PostMapping("{journalId}/media")
    public ResponseEntity<?> attachMedia(@PathVariable Long journalId, @RequestBody MediaIdDTO mediaIds)
            throws IOException {
        return ResponseEntity.ok(service.attachMedia(journalId, mediaIds));
    }

    @DeleteMapping("{journalId}/media")
    public ResponseEntity<?> deattachMedia(@PathVariable Long journalId, @RequestBody MediaIdDTO mediaIds)
            throws IOException {
        return ResponseEntity.ok(service.deattachMedia(journalId, mediaIds));
    }

}
