package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ResearchPaperModelAssembler
                implements RepresentationModelAssembler<ResearchPaper, EntityModel<ResearchPaper>> {

        @Override
        public EntityModel<ResearchPaper> toModel(ResearchPaper researchpaper) {

                return EntityModel.of(
                                researchpaper, //
                                linkTo(methodOn(
                                                ResearchPaperController.class).one(
                                                                researchpaper.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(ResearchPaperController.class).all())
                                                .withRel("researchpapers"),
                                linkTo(methodOn(ResearchPaperController.class).newResearchPaper(null, null))
                                                .withRel("create"),
                                linkTo(methodOn(ResearchPaperController.class)
                                                .validateResearchPaper(researchpaper.getId(), null))
                                                .withRel("validate"),
                                linkTo(methodOn(ResearchPaperController.class)
                                                .requestResearchPaperAccess(researchpaper.getId(), null))
                                                .withRel("requestAccess"),
                                linkTo(methodOn(ResearchPaperController.class)
                                                .respondToRequestResearchPaperAccess(researchpaper.getId(), null, null))
                                                .withRel("respondToAccess"),
                                linkTo(methodOn(ResearchPaperController.class)
                                                .updateResearchPaperPartially(researchpaper.getId(), null))
                                                .withRel("update"),
                                linkTo(methodOn(ResearchPaperController.class)
                                                .deleteResearchPaper(researchpaper.getId()))
                                                .withRel("delete"));
        }

}
