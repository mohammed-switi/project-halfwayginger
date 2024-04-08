package edu.bethlehem.scinexus.JPARepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.Visibility;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

    Optional<Journal> findByVisibility(Visibility visibility);
}
