package edu.bethlehem.scinexus.Journal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class JournalModelAssembler implements RepresentationModelAssembler<Journal, EntityModel<Journal>> {

        @Override
        public EntityModel<Journal> toModel(Journal journal) {

                return EntityModel.of(
                                journal, //
                                linkTo(methodOn(
                                                JournalController.class).one(journal.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(JournalController.class).all())
                                                .withRel("journals"),
                                linkTo(methodOn(JournalController.class).addContributorNew(journal.getId(), null))
                                                .withRel("addContributor"),
                                linkTo(methodOn(JournalController.class).removeContributorNew(journal.getId(), null))
                                                .withRel("removeContributor"),
                                linkTo(methodOn(JournalController.class).attachMedia(journal.getId(), null))
                                                .withRel("attachMedia"),
                                linkTo(methodOn(JournalController.class).deattachMedia(journal.getId(), null))
                                                .withRel("deattachMedia"),
                                linkTo(methodOn(JournalController.class).getJournalInteractions(journal.getId()))
                                                .withRel("interactions"),
                                linkTo(methodOn(JournalController.class).getJournalOpinions(journal.getId()))
                                                .withRel("opinions"));
        }

}
