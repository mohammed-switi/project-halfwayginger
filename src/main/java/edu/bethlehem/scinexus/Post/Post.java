package edu.bethlehem.scinexus.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class Post {
    private @Id @GeneratedValue Long id;
    private String content;
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postsAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private Academic publisherAcademic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postsOrganization")
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