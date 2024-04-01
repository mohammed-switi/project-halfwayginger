package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
