package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Post.Visibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Academic.Academic;
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
    private String subject;
    private String title;
    private String language;
    private Integer noOfPages;

    @JdbcTypeCode(SqlTypes.JSON)
    private Organization validatedBy;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> contributors;

    public Journal(String title, String description, String subject, User publisher) {

        this.description = description;
        this.subject = subject;
        this.title = title;

    }

}