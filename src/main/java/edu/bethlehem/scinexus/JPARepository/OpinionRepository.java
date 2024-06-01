package edu.bethlehem.scinexus.JPARepository;

import edu.bethlehem.scinexus.Opinion.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    List<Opinion> findByJournalId(Long journalId);

    List<Opinion> deleteByPapaOpinionId(Long opinionId);

}
