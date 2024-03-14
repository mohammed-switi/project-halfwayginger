package edu.bethlehem.scinexus.Opinion;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Entity
public class Opinion {
    private @Id @GeneratedValue Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    @JdbcTypeCode(SqlTypes.JSON)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_paper")
    @JdbcTypeCode(SqlTypes.JSON)
    private ResearchPaper researchPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article")
    @JdbcTypeCode(SqlTypes.JSON)
    private Article article;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "opinionInteractions")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    public Opinion(String content) {

        this.content = content;
    }

    public Opinion() {
    }
}