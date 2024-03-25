package edu.bethlehem.scinexus.Organization;

import java.util.List;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Organization extends User {

    private @Id @GeneratedValue Long id;

    @Enumerated(EnumType.STRING)
    // @NotBlank(message = "The Organization Type Must Be Determined")
    // @NotNull(message = "The Organization Type Must Be Determined")
    private OrganizationType type;

    // @NotNull(message = "You should Specify if Verified Or Not")
    // @NotBlank(message = "You should Specify if Verified Or Not")
    private Boolean verified;

    @ManyToMany(mappedBy = "validatedBy")
    List<ResearchPaper> validated;

    public Organization(String name, String username, String password, String email, OrganizationType type) {
        super(name, username, password, email);
        this.type = type;
    }

    public Organization(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    public Organization() {
    }
}