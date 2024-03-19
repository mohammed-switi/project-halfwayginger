package edu.bethlehem.scinexus.User;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;

    private String password;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    private Media profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_cover_id")
    private Media profileCover;

    private String bio;
    private String phoneNumber;
    private String fieldOfWork;

    @JdbcTypeCode(SqlTypes.JSON)
    private JSONPObject userSettings;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Journal> journals;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "notifications")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Notification> notifications;

    @ManyToMany
    @JoinTable(name = "journal_user_contributors", joinColumns = @JoinColumn(name = "contributs"), inverseJoinColumns = @JoinColumn(name = "contributors"))
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Journal> contributs;

    @ManyToMany
    @JoinTable(name = "user_link_user", joinColumns = @JoinColumn(name = "linkFrom"), inverseJoinColumns = @JoinColumn(name = "LinkTo"))
    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> links;

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
    public String getUsername() {
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