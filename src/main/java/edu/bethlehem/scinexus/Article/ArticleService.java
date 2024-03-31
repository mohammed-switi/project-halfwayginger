package edu.bethlehem.scinexus.Article;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleNotFoundException;
import edu.bethlehem.scinexus.Article.ArticleRepository;
import edu.bethlehem.scinexus.Article.ArticleRequestDTO;
import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionRepository;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationService;
import edu.bethlehem.scinexus.Opinion.OpinionRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserController;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OpinionRepository opinionRepository;
    private final InteractionRepository interactionRepository;
    private final ArticleModelAssembler assembler;
    private final NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public Article convertArticleDtoToArticleEntity(Authentication authentication,
            ArticleRequestDTO ArticleRequestDTO) {

        return Article.builder()
                .content(ArticleRequestDTO.getContent())
                .visibility(ArticleRequestDTO.getVisibility())
                .title(ArticleRequestDTO.getTitle())
                .subject(ArticleRequestDTO.getSubject())
                .publisher(getUserId(jwtService.extractId(authentication)))
                .build();
    }

    public User getUserId(long id) {
        logger.trace("Getting User by ID");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<Article> findArticleById(Long ArticleId) {
        logger.trace("Finding Article by ID");
        Article article = articleRepository.findById(
                ArticleId)
                .orElseThrow(() -> new ArticleNotFoundException(ArticleId, HttpStatus.NOT_FOUND));

        return assembler.toModel(article);
    }

    // We Should Specify An Admin Authority To get All Articles
    public CollectionModel<EntityModel<Article>> findAllArticles() {
        logger.trace("Finding All Articles");
        List<EntityModel<Article>> articles = articleRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
    }

    public Article saveArticle(Article article) {
        logger.trace("Saving Article");
        return articleRepository.save(article);
    }

    public EntityModel<Article> createArticle(ArticleRequestDTO newArticleRequestDTO,
            Authentication authentication) {
        logger.trace("Creating Article");
        Article article = convertArticleDtoToArticleEntity(authentication,
                newArticleRequestDTO);
        article = saveArticle(article);
        notificationService.notifyLinks(
                ((User) authentication.getPrincipal()).getId(),
                "A new article from your links",
                linkTo(methodOn(
                        ArticleController.class).one(article.getId())));
        return assembler.toModel(article);
    }

    public EntityModel<Article> updateArticle(Long articleId,
            ArticleRequestDTO newArticleRequestDTO) {
        logger.trace("Updating Article");
        return articleRepository.findById(
                articleId)
                .map(article -> {
                    article.setContent(newArticleRequestDTO.getContent());
                    article.setVisibility(newArticleRequestDTO.getVisibility());
                    article.setTitle(newArticleRequestDTO.getTitle());
                    article.setSubject(newArticleRequestDTO.getSubject());
                    return assembler.toModel(articleRepository.save(article));
                })
                .orElseThrow(() -> new ArticleNotFoundException(
                        articleId, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    public EntityModel<Article> updateArticlePartially(Long articleId,
            ArtilceRequestPatchDTO newArticleRequestDTO) {
        logger.trace("Partially Updating Article");
        Article article = articleRepository.findById(articleId)
                .orElseThrow(
                        () -> new ArticleNotFoundException(articleId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : ArtilceRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newArticleRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = Article.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(article, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(articleRepository.save(article));

    }

    @Transactional
    public void deleteArticle(Long articleId) {
        logger.trace("Deleting Article");
        Article article = articleRepository.findById(articleId)
                .orElseThrow(
                        () -> new ArticleNotFoundException(articleId, HttpStatus.UNPROCESSABLE_ENTITY));

        // i had to add these lines to delete the opinions and interactions of the
        // article
        // cuz the cascading didn't work
        article.getOpinions().forEach(opinion -> opinionRepository.delete(opinion));
        article.getInteractions().forEach(interaction -> interactionRepository.delete(interaction));

        articleRepository.delete(article);
    }

}
