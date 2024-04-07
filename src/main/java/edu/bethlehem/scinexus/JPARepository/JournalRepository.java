package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {


    Optional<Journal> findByVisibility(Visibility visibility);
}
