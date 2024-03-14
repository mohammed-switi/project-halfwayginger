package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import edu.bethlehem.scinexus.User.User;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Academic extends User {
    private @Id @GeneratedValue Long id;
    private String badge;
    private String education;

    @OneToOne
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private Position position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Post> postsAcademic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Post> researchPaperPublisherAcademic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Post> articlePublisherAcademic;

    @ManyToMany(mappedBy = "requestsForAccess")
    List<ResearchPaper> requestsResearchPapers;

    public Academic(String badge, String education, Organization organization, Position position) {
        this.badge = badge;
        this.education = education;
        this.organization = organization;
        this.position = position;
    }

    public Academic(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    public Academic() {
    }
}