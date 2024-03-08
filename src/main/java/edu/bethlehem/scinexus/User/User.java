
package edu.bethlehem.scinexus.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import lombok.Data;

import edu.bethlehem.scinexus.Media.Media;

// @Entity
// @Table(name = "user")
// @Data
public class User {
    private @Id @GeneratedValue Long id;

    private String name;
    private String username;
    private String password;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media profileCover;

    private String bio;
    private String phoneNumber;
    private String fieldOfWork;

    private String userSettings;

    public User(Long userId, String name, String username, String password, String email,
            Media profilePicture,
            Media profileCover, String bio, String phoneNumber, String fieldOfWork, String userSettings) {
        this.id = userId;
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

    public User() {
    }
}