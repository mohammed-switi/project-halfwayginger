package edu.bethlehem.scinexus.Organization;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Organization extends User {

    private @Id @GeneratedValue Long id;
    private String type;
    private Boolean verified;

    public Organization(String name, String username, String password, String email, String type) {
        super(name, username, password, email);
        this.type = type;
    }

    public Organization(String name, String username, String password, String email) {
        super(name, username, password, email);
    }

    public Organization() {
    }
}