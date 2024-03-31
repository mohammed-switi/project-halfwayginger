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
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("research_paper")
public class ResearchPaper extends Journal {

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

    @OneToMany
    @JoinColumn(name = "research_paper_id")
    @JsonIgnore
    private Set<UserResearchPaperRequest> requestsForAccess;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "research_paper_validated_by_organization", joinColumns = @JoinColumn(name = "validated"), inverseJoinColumns = @JoinColumn(name = "validated_research_papers"))
    private List<User> validatedBy;

    public ResearchPaper(String title, String content, String description, String subject, User publisher) {
        super(content, publisher);
        this.title = title;
        this.subject = subject;
        this.description = description;

    }

    // @Override
    // public String toString() {
    // return "ResearchPaper{" +
    // "language=" + language +
    // ", description='" + description + '\'' +
    // ", title='" + title + '\'' +
    // ", subject='" + subject + '\'' +
    // ", noOfPages=" + noOfPages +
    // ", requestsForAccess=" + requestsForAccess +
    // ", validatedBy=" + validatedBy +
    // '}';
    // }
}