package edu.bethlehem.scinexus.UserResearchPaper;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class ResearchPaperRequestKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "research_paper_id")
    private Long researchPaperId;

}
