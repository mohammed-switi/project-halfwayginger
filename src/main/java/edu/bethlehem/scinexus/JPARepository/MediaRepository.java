package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.User.User;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Media findByIdAndOwner(Long id, User user);
}
