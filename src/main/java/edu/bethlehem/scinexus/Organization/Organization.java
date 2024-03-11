package edu.bethlehem.scinexus.Organization;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Organization extends User {

    private @Id @GeneratedValue Long id;
    private String type;
    private Boolean verified;

    public Organization(String name, String username, String password, String email, String type, Boolean verified) {
        super(name, username, password, email);
        this.type = type;
        this.verified = verified;
    }

    public Organization() {
    }
}