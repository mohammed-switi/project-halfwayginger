package edu.bethlehem.scinexus.JPARepository;

import java.util.*;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Journal.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublisherId(Long userId);

    Article findByIdAndPublisherId(Long id, Long userId);

    Optional<Article> findByVisibility(Visibility visibility);
}
