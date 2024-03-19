package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.Post.Visibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Journal {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;

    private String description;

    private Integer interactionCount;
    private Integer opinionCount;
    private String content;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "journal")
    private List<Media> medias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reShare")
    @JdbcTypeCode(SqlTypes.JSON)
    private Journal reShare;

    @ManyToMany(mappedBy = "contributs")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> contributors;

    public Journal(String content, User publisher) {
        this.publisher = publisher;
        this.content = content;
    }

}