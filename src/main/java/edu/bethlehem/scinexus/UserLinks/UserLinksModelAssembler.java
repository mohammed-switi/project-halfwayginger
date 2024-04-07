package edu.bethlehem.scinexus.UserLinks;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class UserLinksModelAssembler implements RepresentationModelAssembler<UserLinks, EntityModel<UserLinks>> {

    @Override
    public EntityModel<UserLinks> toModel(UserLinks user) {

        return EntityModel.of(
                user
        // , //
        // linkTo(methodOn(
        // UserLinks.class).one(
        // user.getId()))
        // .withSelfRel(),
        // linkTo(methodOn(
        // UserLinks.class).all()).withRel(
        // "userLinks")
        );
    }

}
