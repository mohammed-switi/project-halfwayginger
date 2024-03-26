package edu.bethlehem.scinexus.Journal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Journal implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;

    // to be moved to researchpaper

    @Min(value = 0)
    private Integer interactionCount = 0;

    @Min(value = 0)
    private Integer opinionCount = 0;

    @NotNull(message = "The Journal Content Shouldn't Be Null")
    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    // add default value to be private
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Journal Visibility Shouldn't Be Null")
    // @NotBlank(message = "The Journal Visibility Shouldn't Be Empty")
    private Visibility visibility;

    // @NotNull(message = "The Journal Publisher Shouldn't Be Null")
    @ManyToOne(fetch = FetchType.LAZY) // Fetch Type Has been changed From Lazy To Eager,
                                       // Because I get an error when I
    // request one Journal
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonManagedReference
    private User publisher;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.EAGER)
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "journal")
    private List<Media> medias;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reShare")
    @JdbcTypeCode(SqlTypes.JSON)
    private Journal reShare;

    @ManyToMany(mappedBy = "contributs", fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    // @JsonIgnore
    private Set<User> contributors = new HashSet<User>();

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public Journal(String content, User publisher) {
        this.interactionCount = 0;
        this.opinionCount = 0;
        this.publisher = publisher;
        this.content = content;

    }

    public void removeInteraction(Interaction interaction) {
        this.interactions.remove(interaction);
        interactionCount--;
    }

    public void removeOpinion(Opinion opinion) {
        this.opinions.remove(opinion);
        opinionCount--;
    }

    public void addInteraction(Interaction interaction) {
        this.interactions.add(interaction);
        interactionCount++;
    }

    public void addOpinion(Opinion opinion) {
        this.opinions.add(opinion);
        opinionCount++;
    }

}