package edu.bethlehem.scinexus.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;
import edu.bethlehem.scinexus.Conditional.Conditional;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.Nullable;

import java.util.*;

// @Entity
// @Table(name = "_user")

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSerialize
@Builder
@Getter
@Setter
@Conditional(selected = "role", values = { "ACADEMIC" }, required = {
        "education", "badge", "position" })
@Conditional(selected = "role", values = { "ORGANIZATION" }, required = {
        "type" })
public class User implements UserDetailsImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "First Name is mandatory")
    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 30, message = "First Name must be between 2 and 30 Characters")
    private String firstName;

    @NotNull(message = "Last Name is mandatory")
    @NotBlank(message = "Last Name is mandatory")
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(unique = true)
    @NotNull(message = "Username is mandatory")
    @NotBlank(message = "Username is mandatory")
    @Size(min = 1, max = 50)
    private String username;

    @NotNull(message = "Email is mandatory")
    @NotBlank(message = "Email Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9]+[._-]*[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$", message = "It must start with alphanumeric characters."
            +
            "It may contain dots, hyphens, and underscores." +
            "It must have exactly one '@' symbol." +
            "It may contain alphanumeric characters in the domain part." +
            "It may have a maximum of two dots following the '@' symbol")
    @Column(unique = true)
    @Size(min = 3, max = 320, message = "The Email Address should be Between 3 and 320 Characters")
    private String email;

    /*
     * Password pattern validation Will Not be held here because the password is
     * stored in the database in encrypted version
     * However The password vaildation will be in the RegisterRequest class
     */
    @NotNull(message = "Password is Mandatory")
    @NotBlank(message = "Password is Mandatory")
    @JsonIgnore
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    @JsonIgnore
    private Media profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_cover_id")
    @JsonIgnore
    private Media profileCover;

    @Size(min = 0, max = 220, message = "The bio should be in maximum 220 characters")
    private String bio;

    @Column(unique = true)
    @NotNull(message = "Phone Number is Mandatory")
    @NotBlank(message = "Phone Number is Mandatory")
    // This Regex Status that phone number is valid in any of these formats :
    // (123) 456-7890
    // (123)456-7890
    // 123-456-7890
    // 123.456.7890
    // 1234567890
    // 1-123-123-1234
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?([-. (]*(\\d{3})[-. )]*)?((\\d{3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)\\s*$", message = "Phone Number is Not Valid")
    private String phoneNumber;

    @Nullable
    @Size(max = 320)
    private String fieldOfWork;

    // @NotNull
    private Boolean locked;
    // @NotNull
    private Boolean enabled;

    @JdbcTypeCode(SqlTypes.JSON)
    private JSONPObject userSettings;

    @NotNull
    @Enumerated(EnumType.STRING)

    private Role role;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private Set<Journal> journals = new HashSet<Journal>();

    // @OneToMany(fetch = FetchType.EAGER)
    // @JoinColumn(name = "notifications")
    // @JdbcTypeCode(SqlTypes.JSON)
    // @JsonIgnore
    // private List<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "journal_user_contributors", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "journal_id"))
    @JsonBackReference
    private Set<Journal> contributedJournals = new HashSet<>();

    // Academic Specific Fields
    private String badge;

    private String education;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "organizationId")
    @Nullable
    @JsonIgnore
    private User organization;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<UserResearchPaperRequest> requestsResearchPapers;

    // Organization Specific Fields
    @Enumerated(EnumType.STRING)
    // @NotBlank(message = "The Organization Type Must Be Determined")
    // @NotNull(message = "The Organization Type Must Be Determined")
    private OrganizationType type;

    // @NotBlank(message = "You should Specify if Verified Or Not")
    // @NotNull(message = "You should Specify if Verified Or Not")
    private Boolean verified;

    @ManyToMany(mappedBy = "validatedBy")
    List<ResearchPaper> validated;

    public User(String firstName, String username, String password, String email, Boolean locked, Boolean enabled) {
        this.firstName = firstName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.locked = locked;
        this.enabled = enabled;

    }

    public void addJournal(Journal journal) {
        if (journals == null) {
            journals = new HashSet<>();
        }
        if (journals.contains(journal)) {
            return;
        }

        journals.add(journal);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

}