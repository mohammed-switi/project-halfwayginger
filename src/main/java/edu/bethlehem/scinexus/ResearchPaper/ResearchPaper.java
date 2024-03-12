package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.*;
import lombok.Data;
import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class ResearchPaper extends Journal {
    private @Id @GeneratedValue Long id;
    private String description;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "researchPaperPublisherAcademic")
    @JdbcTypeCode(SqlTypes.JSON)
    private Organization publisherAcademic;

    public ResearchPaper(String description, Organization validatedBy, String subject, String language,
            Integer noOfPages) {
        this.description = description;
        this.validatedBy = validatedBy;

    }

    public ResearchPaper(String name, String description, String subject, String title, String language, User publisher,
            Integer noOfPages, String visibility, Organization validatedBy) {
        super(name, description, subject, title, language, publisher, noOfPages, visibility);
        this.description = description;
        this.validatedBy = validatedBy;

    }

    public ResearchPaper() {
    }
}