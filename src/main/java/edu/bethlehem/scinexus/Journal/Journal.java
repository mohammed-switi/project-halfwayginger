package edu.bethlehem.scinexus.Journal;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> contributors;

    public Journal(String name, String description, String subject, String title, String language, User publisher,
            Integer noOfPages, Organization validatedBy, String visibility, List<User> contributors) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.title = title;
        this.language = language;
        this.publisher = publisher;
        this.noOfPages = noOfPages;
        this.validatedBy = validatedBy;
        this.visibility = visibility;
        this.contributors = contributors;

    }

    public Journal(String name, String description, String subject, String title, String language, User publisher,
            Integer noOfPages, String visibility) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.title = title;
        this.language = language;
        this.publisher = publisher;
        this.noOfPages = noOfPages;
        this.visibility = visibility;
    }

    public Journal() {
    }
}