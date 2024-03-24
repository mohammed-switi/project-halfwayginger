package edu.bethlehem.scinexus.Journal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Journal implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;

    private String description;

    private Integer interactionCount;
    private Integer opinionCount;
    private String content;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
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
        this.publisher = publisher;
        this.content = content;
    }

}