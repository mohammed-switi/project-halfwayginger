package edu.bethlehem.scinexus.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

        @Override
        public EntityModel<User> toModel(User user) {

                return EntityModel.of(
                                user, //
                                linkTo(methodOn(UserController.class).one(user.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(UserController.class).all())
                                                .withRel("users"),
                                linkTo(methodOn(UserController.class).updateUserPartially(user.getId(), null))
                                                .withRel("update"),
                                linkTo(methodOn(UserController.class).linkUser(null, null))
                                                .withRel("linkUser"),
                                linkTo(methodOn(UserController.class).respondToLinkage(null, null, null))
                                                .withRel("respondToLinkage"),
                                linkTo(methodOn(UserController.class).unlinkUser(null, null))
                                                .withRel("unlink"),
                                linkTo(methodOn(UserController.class).getUserArticles(null))
                                                .withRel("articles"),
                                linkTo(methodOn(UserController.class).getUserArticle(null, null))
                                                .withRel("article"),
                                linkTo(methodOn(UserController.class).getUserResearchPapers(null))
                                                .withRel("researchPapers"),
                                linkTo(methodOn(UserController.class).getUserResearchPaper(null, null))
                                                .withRel("researchPaper"),
                                linkTo(methodOn(UserController.class).getUserNotifications(null))
                                                .withRel("notifications"));
        }

}
