package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class ResearchPaperModelAssembler implements RepresentationModelAssembler<ResearchPaper, EntityModel<ResearchPaper>> {

    @Override
    public EntityModel<ResearchPaper> toModel(ResearchPaper researchpaper) {

        return EntityModel.of(
                researchpaper, //
                linkTo(methodOn(
                        ResearchPaperController.class).one(
                                researchpaper.getId()))
                        .withSelfRel(),
                linkTo(methodOn(ResearchPaperController.class).all()).withRel(
                        "+researchpapers"));
    }

}
