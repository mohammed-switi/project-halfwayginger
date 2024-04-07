package edu.bethlehem.scinexus.Interaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class InteractionModelAssembler implements RepresentationModelAssembler<Interaction, EntityModel<Interaction>> {

        @Override
        public EntityModel<Interaction> toModel(Interaction interaction) {

                return EntityModel.of(
                                interaction, //
                                linkTo(methodOn(
                                                InteractionController.class).one(interaction.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(InteractionController.class).all())
                                                .withRel("interactions"),
                                linkTo(methodOn(InteractionController.class)
                                                .updateInteractionPartially(interaction.getId(), null))
                                                .withRel("update"),
                                linkTo(methodOn(InteractionController.class).deleteInteraction(interaction.getId(),
                                                null))
                                                .withRel("delete"),
                                linkTo(methodOn(InteractionController.class).addOpinionInteraction(interaction.getId(),
                                                null, null))
                                                .withRel("addToOpinion"),
                                linkTo(methodOn(InteractionController.class).addJournalInteraction(interaction.getId(),
                                                null, null))
                                                .withRel("addToJournal"));
        }

}
