package edu.bethlehem.scinexus.Academic;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class AcademicModelAssembler implements RepresentationModelAssembler<Academic, EntityModel<Academic>> {

    @Override
    public EntityModel<Academic> toModel(Academic academic) {

        return EntityModel.of(
                academic, //
                linkTo(methodOn(
                        AcademicController.class).one(
                                academic.getId()))
                        .withSelfRel(),
                linkTo(methodOn(AcademicController.class).all()).withRel(
                        "+academics"));
    }

}
