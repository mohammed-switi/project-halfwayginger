package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class ResearchPaper extends Journal {
    private @Id @GeneratedValue Long id;

    @OneToOne
    @JdbcTypeCode(SqlTypes.JSON)
    private Organization validatedBy;

    @ManyToMany
    @JoinTable(name = "research_paper_access_request_academics", joinColumns = @JoinColumn(name = "requestsForAccess"), inverseJoinColumns = @JoinColumn(name = "requestsResearchPapers"))
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Academic> requestsForAccess;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "research_paper")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerResearchPaper")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Media> media;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "researchPaperInteractions")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    public ResearchPaper(Organization validatedBy, String subject, String language,
            Integer noOfPages) {

        this.validatedBy = validatedBy;

    }

    public ResearchPaper(String title, String description, String subject, Academic publisherAcademic) {
        super(title, description, subject, publisherAcademic);

    }

    public ResearchPaper() {
    }
}