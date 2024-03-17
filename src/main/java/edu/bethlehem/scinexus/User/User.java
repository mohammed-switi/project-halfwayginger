package edu.bethlehem.scinexus.User;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import edu.bethlehem.scinexus.Media.Media;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
// @Entity
// @Table(name = "_user")

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

//@MappedSuperclass
public  class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;
    //@JsonIgnore
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

    @Enumerated(EnumType.STRING)
    private Role role;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername(){
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}