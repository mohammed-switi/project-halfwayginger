package edu.bethlehem.scinexus.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {


    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    @Override
    public @NotNull EntityModel<Post> toModel(@NotNull Post post) {
        return EntityModel.of(
                post, //
                linkTo(methodOn(
                        PostController.class).one(
                                post.getId(),authentication))
                        .withSelfRel(),
                linkTo(methodOn(PostController.class).all()).withRel(
                        "+posts"));
    }

}
