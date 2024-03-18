package edu.bethlehem.scinexus.Journal;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;

@Data
@MappedSuperclass
public class Journal {
    private @Id @GeneratedValue Long id;

    private String name;
    private String description;
    private String subject;
    private String title;
    private String language;
    private Integer noOfPages;

    @JdbcTypeCode(SqlTypes.JSON)
    private Organization validatedBy;

    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academicPublisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private Academic academicPublisher;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> contributors;

    public Journal(String name, String description, String subject, String title, String language,
            Academic academicPublisher,
            Integer noOfPages, Organization validatedBy, String visibility, List<User> contributors) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.title = title;
        this.language = language;
        this.academicPublisher = academicPublisher;
        this.noOfPages = noOfPages;
        this.validatedBy = validatedBy;
        this.visibility = visibility;
        this.contributors = contributors;

    }

    public Journal(String title, String description, String subject, Academic academicPublisher) {

        this.description = description;
        this.subject = subject;
        this.title = title;
        this.academicPublisher = academicPublisher;

    }

    public Journal() {
    }
}