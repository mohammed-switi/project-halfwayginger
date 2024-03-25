package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import edu.bethlehem.scinexus.User.User;

import java.util.List;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Academic extends User {
    private @Id @GeneratedValue Long id;

    private String badge;

    @NotNull(message = "Education Shouldn't be Null")
    @NotBlank(message = "Education Shouldn't be Empty")
    private String education;

    @OneToOne
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "organization_id")
    @Nullable
    private Organization organization;

    @NotNull(message = "The Academic Position Should Be Specified")
    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToMany(mappedBy = "requestsForAccess")
    List<ResearchPaper> requestsResearchPapers;

    public Academic(String badge, String education, Organization organization, Position position) {
        this.badge = badge;
        this.education = education;
        this.organization = organization;
        this.position = position;
    }

    public Academic(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    public Academic() {
    }
}