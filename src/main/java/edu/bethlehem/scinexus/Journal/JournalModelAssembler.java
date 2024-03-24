package edu.bethlehem.scinexus.Journal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class JournalModelAssembler implements RepresentationModelAssembler<Journal, EntityModel<Journal>> {

        @Override
        public EntityModel<Journal> toModel(Journal journal) {

                return EntityModel.of(
                                journal, //
                                linkTo(methodOn(
                                                JournalController.class).one(
                                                                journal.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(JournalController.class).all()).withRel(
                                                "journals"));
        }

}
