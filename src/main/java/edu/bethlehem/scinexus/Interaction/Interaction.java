package edu.bethlehem.scinexus.Interaction;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Interaction {
    private @Id @GeneratedValue Long id;
    private Long interactionId;
    private InteractionType type;

    @ManyToOne
    @JoinColumn(name = "opinionInteractions")
    private Opinion opinion;

    @ManyToOne
    @JoinColumn(name = "postInteractions")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "researchPaperInteractions")
    private ResearchPaper researchPaper;

    @ManyToOne
    @JoinColumn(name = "articleInteractions")
    private Article article;

    public Interaction(Long interactionId, InteractionType type) {
        this.interactionId = interactionId;
        this.type = type;
    }

    public Interaction() {
    }
}