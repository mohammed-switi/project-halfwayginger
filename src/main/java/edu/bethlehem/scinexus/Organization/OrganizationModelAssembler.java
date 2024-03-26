package edu.bethlehem.scinexus.Organization;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import edu.bethlehem.scinexus.User.User;

@Component
class OrganizationModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

        @Override
        public EntityModel<User> toModel(User organization) {

                return EntityModel.of(
                                organization, //
                                linkTo(methodOn(
                                                OrganizationController.class).one(
                                                                organization.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(OrganizationController.class).all()).withRel(
                                                "+organizations"));
        }

}
