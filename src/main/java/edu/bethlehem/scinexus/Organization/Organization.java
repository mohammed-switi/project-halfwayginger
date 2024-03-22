package edu.bethlehem.scinexus.Organization;

import java.util.List;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Organization extends User {

    private @Id @GeneratedValue Long id;

    @Enumerated(EnumType.STRING)
    private OrganizationType type;
    private Boolean verified;

    @ManyToMany(mappedBy = "validatedBy")
    List<ResearchPaper> validateds;

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