package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.Status;
import edu.bethlehem.scinexus.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    Optional<User> findByStatus(Status status);
    Optional<User> findById(Long id);

    List<User> findAllByRole(Role role);

    List<User> findAll();

    Optional<User> findByIdAndRole(Long id, Role role);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);


    @Query("SELECT DISTINCT ul3.linksFrom " +
            "FROM UserLinks ul1 " +
            "JOIN UserLinks ul2 ON ul1.linksTo.id = ul2.linksFrom.id " +
            "JOIN UserLinks ul3 ON ul2.linksTo.id = ul3.linksFrom.id " +
            "WHERE ul1.linksFrom.id = :userId " +
            "AND ul1.accepted = TRUE " +
            "AND ul2.accepted = TRUE " +
            "AND ul3.accepted = TRUE " +
            "AND ul3.linksTo.id != :userId " +
            "AND ul3.linksFrom.id NOT IN ( " +
            "    SELECT ul4.linksTo.id " +
            "    FROM UserLinks ul4 " +
            "    WHERE ul4.linksFrom.id = :userId " +
            "    AND ul4.accepted = TRUE " +
            ")")
    List<User> findUsersByMutualConnections(@Param("userId") Long userId);

    @Query("SELECT u FROM User u JOIN u.skills s WHERE s IN :skills AND u.id <> :userId GROUP BY u.id ORDER BY COUNT(s) DESC")
    List<User> findUsersBySharedSkills(Long userId, List<String> skills);
}
