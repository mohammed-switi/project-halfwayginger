package edu.bethlehem.scinexus.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.UserResearchPaper.ResearchPaperRequestKey;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;

public interface UserResearchPaperRequestRepository
                extends JpaRepository<UserResearchPaperRequest, ResearchPaperRequestKey> {
        UserResearchPaperRequest findByUserAndResearchPaper(User user, ResearchPaper rp);
}