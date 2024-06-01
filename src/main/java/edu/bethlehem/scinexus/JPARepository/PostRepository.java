package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPublisherId(Long userId);

    Optional<Post> findByVisibility(Visibility visibility);
}
