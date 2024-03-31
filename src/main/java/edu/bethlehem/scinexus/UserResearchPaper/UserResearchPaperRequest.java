package edu.bethlehem.scinexus.UserResearchPaper;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResearchPaperRequest {
    @EmbeddedId
    ResearchPaperRequestKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("researchPaperId")
    @JoinColumn(name = "research_paper_id")
    private ResearchPaper researchPaper;

    private boolean isAccepted = false;

}
