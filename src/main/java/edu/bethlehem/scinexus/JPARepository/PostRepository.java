package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByVisibility(Visibility visibility);

    List<Post> findByPublisherId(Long userId);

}
