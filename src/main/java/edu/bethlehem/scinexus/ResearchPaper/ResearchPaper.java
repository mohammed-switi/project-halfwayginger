package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.*;
import lombok.Data;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

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