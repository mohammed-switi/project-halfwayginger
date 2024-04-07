package edu.bethlehem.scinexus.Article;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<Article, EntityModel<Article>> {

        @Override
        public EntityModel<Article> toModel(Article article) {

                return EntityModel.of(
                                article, //
                                linkTo(methodOn(
                                                ArticleController.class).one(
                                                                article.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(ArticleController.class).all())
                                                .withRel("articles"),
                                linkTo(methodOn(ArticleController.class).newArticle(null, null))
                                                .withRel("create"),
                                linkTo(methodOn(ArticleController.class).updateArticlePartially(article.getId(), null))
                                                .withRel("update"),
                                linkTo(methodOn(ArticleController.class).deleteArticle(article.getId()))
                                                .withRel("delete"));
        }

}
