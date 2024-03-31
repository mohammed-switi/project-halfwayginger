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
                                                InteractionController.class).one(
                                                                interaction.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(InteractionController.class).all()).withRel(
                                                "+interactions"));
        }

}
