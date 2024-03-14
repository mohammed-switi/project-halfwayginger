package edu.bethlehem.scinexus.Media;

import org.hibernate.annotations.JdbcTypeCode;

import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Column()
    private @Id @GeneratedValue Long mediaId;
    private MediaType type;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerPost")
    @JdbcTypeCode(SqlTypes.JSON)
    private Post ownerPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerArticle")
    @JdbcTypeCode(SqlTypes.JSON)
    private Article ownerArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerResearchPaper")
    @JdbcTypeCode(SqlTypes.JSON)
    private ResearchPaper ownerResearchPaper;

    public Media(Long mediaId, MediaType type, String path) {
        this.mediaId = mediaId;
        this.type = type;
        this.path = path;
    }

    public Media() {
    }
}