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


    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteByIdCustom(@Param("id") Long id);

}
