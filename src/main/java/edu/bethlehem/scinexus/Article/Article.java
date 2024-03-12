package edu.bethlehem.scinexus.Article;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;
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
public class Article extends Journal {
    private @Id @GeneratedValue Long id;
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "article")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articlePublisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private Organization publisherAcademic;

    public Article(String name, String description, String subject, String title, String language, User publisher,
            Integer noOfPages, String visibility, Organization validatedBy, String content) {
        super(name, description, subject, title, language, publisher, noOfPages, visibility);
        this.content = content;

    }

    public Article() {
    }
}