package edu.bethlehem.scinexus.Opinion;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.mapping.List;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

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
    private java.util.List<Opinion> opinions;

    public Opinion(String content) {

        this.content = content;
    }

    public Opinion() {
    }
}