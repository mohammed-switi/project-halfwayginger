package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.Organization.Organization;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import edu.bethlehem.scinexus.User.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class Academic extends User {
    private @Id @GeneratedValue Long id;
    private String badge;
    private String education;

    @OneToOne
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private Position position;

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