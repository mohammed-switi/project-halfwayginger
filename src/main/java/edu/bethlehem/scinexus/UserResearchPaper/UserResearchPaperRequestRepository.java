package edu.bethlehem.scinexus.UserResearchPaper;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResearchPaperRequestRepository
        extends JpaRepository<UserResearchPaperRequest, ResearchPaperRequestKey> {

}