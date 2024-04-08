package edu.bethlehem.scinexus.JPARepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;

public interface ResearchPaperRepository extends JpaRepository<ResearchPaper, Long> {
    ResearchPaper findByIdAndPublisherId(Long id, Long userId);

    List<ResearchPaper> findByPublisherId(Long userId);
}
