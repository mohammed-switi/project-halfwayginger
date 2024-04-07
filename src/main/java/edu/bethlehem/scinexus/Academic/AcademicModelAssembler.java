package edu.bethlehem.scinexus.Academic;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import edu.bethlehem.scinexus.User.User;

@Component
class AcademicModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

        @Override
        public EntityModel<User> toModel(User academic) {

                return EntityModel.of(
                                academic, //
                                linkTo(methodOn(
                                                AcademicController.class).one(
                                                                academic.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(AcademicController.class).all()).withRel(
                                                "academics"),
                                linkTo(methodOn(AcademicController.class).updateUserPartially(academic.getId(), null))
                                                .withRel("update")

                );
        }

}
