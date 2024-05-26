package edu.bethlehem.scinexus.Journal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "journal_type")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)

public class Journal implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Min(value = 0)
    @Default
    private Integer interactionsCount = 0;

    @Min(value = 0)
    @Default
    private Integer opinionsCount = 0;

    @NotNull(message = "The Journal Content Shouldn't Be Null")
    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    // add default value to be private
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Journal Visibility Shouldn't Be Null")
    // @NotBlank(message = "The Journal Visibility Shouldn't Be Empty")
    private Visibility visibility;

    // @NotNull(message = "The Journal Publisher Shouldn't Be Null")
    @ManyToOne(fetch = FetchType.EAGER) // Fetch Type Has been changed From Lazy To Eager,
    // Because I get an error when I
    // request one Journal
    @JoinColumn(name = "publisher", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonManagedReference

    private User publisher;

    // It was lazy
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private Set<Interaction> interactions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "journal")
    // @JsonBackReference
    private List<Media> medias;

    // @ManyToMany(mappedBy = "contributors", fetch = FetchType.EAGER)
    // @JdbcTypeCode(SqlTypes.JSON)
    // // @JsonIgnore
    // private Set<User> contributors;

    @ManyToMany(mappedBy = "contributedJournals", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonManagedReference
    @Default
    @JsonIdentityReference(alwaysAsId = true)
    private Set<User> contributors = new HashSet<>();

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public Journal(String content, User publisher) {
        this.interactionsCount = 0;
        this.opinionsCount = 0;
        this.publisher = publisher;
        this.content = content;

    }

    public void removeInteraction() {
        interactionsCount--;
    }

    public void addInteraction() {
        interactionsCount++;
    }

    public void removeOpinion() {
        opinionsCount--;
    }

    public void addOpinion() {
        opinionsCount++;
    }

}