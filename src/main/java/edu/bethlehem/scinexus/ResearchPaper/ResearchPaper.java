package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.User.User;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ResearchPaper extends Journal {
    private @Id @GeneratedValue Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Research Paper Language Can't Be Null")
    private ResearchLanguage language;

    @NotNull(message = "The Journal Description Shouldn't Be Null")
    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;

    @NotNull(message = "The Research Paper Title Can't Be Null")
    @NotBlank(message = "The Research Paper Title Should Be Specified")
    private String title;

    @NotNull(message = "The Research Paper Subject Can't Be Null")
    @NotBlank(message = "The Research Paper Subject Should Be Specified")
    private String subject;

    // @NotNull(message = "The Research Paper Number Of Pages Can't Be Null")
    private Integer noOfPages = 0;

    @ManyToMany
    @JoinTable(name = "research_paper_access_request_academics", joinColumns = @JoinColumn(name = "requestsForAccess"), inverseJoinColumns = @JoinColumn(name = "requestsResearchPapers"))
    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> requestsForAccess;

    @ManyToMany
    @JoinTable(name = "research_paper_validated_by_organization", joinColumns = @JoinColumn(name = "validated"), inverseJoinColumns = @JoinColumn(name = "validated_research_papers"))
    private List<User> validatedBy;

    public ResearchPaper(String title, String content, String description, String subject, User publisher) {
        super(content, publisher);
        this.title = title;
        this.subject = subject;
        this.description = description;

    }

}