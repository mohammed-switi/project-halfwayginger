package edu.bethlehem.scinexus.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

        @Override
        public EntityModel<Post> toModel(Post post) {

                return EntityModel.of(
                                post, //
                                linkTo(methodOn(
                                                PostController.class).one(
                                                                post.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(PostController.class).all()).withRel(
                                                "+posts"));
        }

}
