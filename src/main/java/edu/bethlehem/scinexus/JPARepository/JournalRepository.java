package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Journal.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {

}
