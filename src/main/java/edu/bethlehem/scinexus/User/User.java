package edu.bethlehem.scinexus.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.JSONPObject;

import edu.bethlehem.scinexus.Conditional.Conditional;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder.Default;

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
// @ToString
@Conditional(selected = "role", values = { "ACADEMIC" }, required = {
        "education", "badge", "position" })
@Conditional(selected = "role", values = { "ORGANIZATION" }, required = {
        "type"  })
@Table(name = "_user") // Specify the custom table name here

public class User implements UserDetailsImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @NotNull(message = "First Name is mandatory")
    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 30, message = "First Name must be between 2 and 30 Characters")
    private String firstName;

    @Enumerated(EnumType.STRING)
    private Status status;

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
    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Media profilePicture;

    @OneToOne
    @JoinColumn(name = "profile_cover_id")
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
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    @Default
    private Set<Journal> journals = new HashSet<Journal>();

    // @OneToMany(fetch = FetchType.EAGER)
    // @JoinColumn(name = "notifications")
    // @JdbcTypeCode(SqlTypes.JSON)
    // @JsonIgnore
    // private List<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "journal_user_contributors", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "journal_id"))
    @JsonBackReference
    @Default
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


    private String organizationName;
    private String OrganizationAddress;
    private String location;

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

    @ManyToMany(mappedBy = "validatedBy", fetch = FetchType.EAGER)
    @Default
    @JsonBackReference
    @JsonIgnore
    private List<ResearchPaper> validated = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "language")
    private Set<String> languages = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    private String contactEmail;

    private String contactPhoneNumber;

    public User(String firstName, String username, String password, String email, Boolean locked, Boolean enabled) {
        this.firstName = firstName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.locked = locked;
        this.enabled = enabled;

    }

    public List<ResearchPaper> getValidated() {
        return null;
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

    public void addContributedJournal(Journal journal) {
        if (contributedJournals == null) {
            contributedJournals = new HashSet<>();
        }
        if (contributedJournals.contains(journal)) {
            return;
        }

        contributedJournals.add(journal);
    }

    public void addValidatedJournal(ResearchPaper journal) {
        if (validated == null) {
            validated = new ArrayList<>();
        }
        if (validated.contains(journal)) {
            return;
        }

        validated.add(journal);
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