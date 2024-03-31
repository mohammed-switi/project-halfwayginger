package edu.bethlehem.scinexus.Opinion;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OpinionModelAssembler implements RepresentationModelAssembler<Opinion, EntityModel<Opinion>> {

        @Override
        public EntityModel<Opinion> toModel(Opinion opinion) {

                return EntityModel.of(
                                opinion, //
                                linkTo(methodOn(
                                                OpinionController.class).one(
                                                                opinion.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(OpinionController.class).all()).withRel(
                                                "+opinions"));
        }

}
