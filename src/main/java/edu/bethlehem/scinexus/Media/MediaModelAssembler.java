package edu.bethlehem.scinexus.Media;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class MediaModelAssembler implements RepresentationModelAssembler<Media, EntityModel<Media>> {

        @Override
        public EntityModel<Media> toModel(Media media) {

                return EntityModel.of(
                                media, //
                                linkTo(methodOn(
                                                MediaController.class).one(
                                                                media.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(MediaController.class).all()).withRel(
                                                "+medias"));
        }

}
