package edu.bethlehem.scinexus.Organization;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Post.Post;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherOrganization")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Post> postsOrganization;

    public Organization(String name, String username, String password, String email, String type, Boolean verified) {
        super(name, username, password, email);
        this.type = type;
        this.verified = verified;
    }

    public Organization() {
    }
}