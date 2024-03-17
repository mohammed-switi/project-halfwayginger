package edu.bethlehem.scinexus.User;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.*;

import lombok.Data;

import edu.bethlehem.scinexus.Media.Media;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

// @Entity
// @Table(name = "_user")

@Data
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private Media profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_cover_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private Media profileCover;

    private String bio;
    private String phoneNumber;
    private String fieldOfWork;

    @JdbcTypeCode(SqlTypes.JSON)

    private JSONPObject userSettings;

    public User(String name, String username, String password, String email,
            Media profilePicture,
            Media profileCover, String bio, String phoneNumber, String fieldOfWork, JSONPObject userSettings) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePicture = profilePicture;
        this.profileCover = profileCover;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.fieldOfWork = fieldOfWork;
        this.userSettings = userSettings;
    }

    public User(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }
}