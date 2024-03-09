package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.*;
import lombok.Data;

import edu.bethlehem.scinexus.Organization.Organization;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class ResearchPaper {
    private @Id @GeneratedValue Long id;
    private String description;

    @OneToOne
    @JdbcTypeCode(SqlTypes.JSON)
    private Organization validatedBy;
    private String subject;
    private String language;
    private Integer noOfPages;

    public ResearchPaper(String description, Organization validatedBy, String subject, String language,
            Integer noOfPages) {
        this.description = description;
        this.validatedBy = validatedBy;
        this.subject = subject;
        this.language = language;
        this.noOfPages = noOfPages;
    }

    public ResearchPaper() {
    }
}