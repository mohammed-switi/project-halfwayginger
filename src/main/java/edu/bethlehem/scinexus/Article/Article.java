package edu.bethlehem.scinexus.Article;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Article extends Journal {
    private @Id @GeneratedValue Long id;
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "article")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerArticle")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Media> media;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "articleInteractions")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    public Article(String title, String description, String subject, Academic publisherAcademic, String content) {
        super(title, description, subject, publisherAcademic);
        this.content = content;
    }

    public Article() {
    }
}