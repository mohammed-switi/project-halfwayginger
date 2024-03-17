package edu.bethlehem.scinexus.Post;

import jakarta.persistence.*;
import lombok.Data;
import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class Post {
    private @Id @GeneratedValue Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private Academic publisherAcademic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisherOrganization")
    @JdbcTypeCode(SqlTypes.JSON)
    private Organization publisherOrganization;

    private Integer interactionCount;
    private Integer opinionsCount;

    @JdbcTypeCode(SqlTypes.JSON)
    private Post reShare;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerPost")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Media> media;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "postInteractions")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    public Post(String content, Visibility visibility, Academic publisherAcademic,
            Integer interactionCount,
            Integer opinionsCount, Post reShare) {

        this.content = content;
        this.visibility = visibility;
        this.publisherAcademic = publisherAcademic;
        this.interactionCount = interactionCount;
        this.opinionsCount = opinionsCount;
        this.reShare = reShare;
    }

    public Post() {
    }
}