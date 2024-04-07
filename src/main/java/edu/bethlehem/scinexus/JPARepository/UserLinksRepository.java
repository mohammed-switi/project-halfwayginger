package edu.bethlehem.scinexus.JPARepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.UserLinks.UserLinks;

public interface UserLinksRepository extends JpaRepository<UserLinks, Long> {

    Boolean existsByLinksToAndLinksFrom(User user1, User user2);

    UserLinks findByLinksFromAndLinksTo(User user1, User user2);

    List<UserLinks> findByLinksFromOrLinksTo(User user1, User user2);
}