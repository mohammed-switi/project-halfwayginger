package edu.bethlehem.scinexus.JPARepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLinksRepository extends JpaRepository<UserLinks, Long> {

    Boolean existsByLinksToAndLinksFrom(User user1, User user2);

    UserLinks findByLinksFromAndLinksTo(User user1, User user2);

    List<UserLinks> findByLinksFromOrLinksTo(User user1, User user2);



    @Query("SELECT ul FROM UserLinks ul " +
            "WHERE (ul.linksFrom.id = :userId OR ul.linksTo.id = :userId) AND " +
            "EXISTS (SELECT 1 FROM UserLinks ul2 " +
            "WHERE (ul2.linksFrom.id = ul.linksTo.id AND ul2.linksTo.id = ul.linksFrom.id) OR " +
            "(ul2.linksFrom.id = ul.linksFrom.id AND ul2.linksTo.id = ul.linksTo.id))")
    List<UserLinks> findMutualConnections(@Param("userId") Long userId);


    @Query("SELECT CASE WHEN COUNT(ul) > 0 THEN true ELSE false END FROM UserLinks ul " +
            "WHERE ((ul.linksFrom.id = :userId1 AND ul.linksTo.id = :userId2) OR " +
            "(ul.linksFrom.id = :userId2 AND ul.linksTo.id = :userId1))")
    boolean areUsersLinked(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}