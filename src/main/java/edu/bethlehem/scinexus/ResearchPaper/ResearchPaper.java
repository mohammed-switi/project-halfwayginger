package edu.bethlehem.scinexus.ResearchPaper;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

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
    @Default
    private Integer noOfPages = 0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "research_paper_id")
    @JsonIgnore
    private Set<UserResearchPaperRequest> requestsForAccess;

    @ManyToMany(fetch = FetchType.EAGER)
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