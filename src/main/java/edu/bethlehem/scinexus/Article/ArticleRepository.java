package edu.bethlehem.scinexus.Article;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublisherId(Long userId);

    Article findByIdAndPublisherId(Long id, Long userId);
}
