package edu.bethlehem.scinexus.UserResearchPaper;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.User;

public interface UserResearchPaperRequestRepository
                extends JpaRepository<UserResearchPaperRequest, ResearchPaperRequestKey> {
        UserResearchPaperRequest findByUserAndResearchPaper(User user, ResearchPaper rp);
}