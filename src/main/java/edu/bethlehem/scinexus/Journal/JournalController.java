package edu.bethlehem.scinexus.Journal;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<CollectionModel<EntityModel<Journal>>> all(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        return ResponseEntity.ok(service.findAllJournals(pageNo, pageSize));
    }

    @PatchMapping("/{journalId}/contributors/{contributorId}")
    public ResponseEntity<?> addContributorNew(@PathVariable Long journalId, @PathVariable Long contributorId) {

        EntityModel<Journal> entityModel = service.addContributor(journalId, contributorId);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
        // return
        // ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
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
