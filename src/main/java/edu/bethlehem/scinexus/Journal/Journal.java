package edu.bethlehem.scinexus.Journal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "journal_type")
public class Journal implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;

    @Min(value = 0)
    private Integer interactionsCount = 0;

    @Min(value = 0)
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private Set<Interaction> interactions;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.EAGER)
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "journal")
    @JsonBackReference
    private List<Media> medias;

    // @ManyToMany(mappedBy = "contributors", fetch = FetchType.EAGER)
    // @JdbcTypeCode(SqlTypes.JSON)
    // // @JsonIgnore
    // private Set<User> contributors;

    @ManyToMany(mappedBy = "contributedJournals", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonManagedReference
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

    public void setOpinions() {

    }

}