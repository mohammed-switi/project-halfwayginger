package edu.bethlehem.scinexus.Journal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.bethlehem.scinexus.Post.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
public class Journal {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Id Long id;


    @NotNull(message = "The Journal Description Shouldn't Be Null")
    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;


    @Min(0)
    private Integer interactionCount=0;

    private Integer opinionCount;

    @NotNull(message = "The Journal Content Shouldn't Be Null")
    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Journal Visibility Shouldn't Be Null")
//    @NotBlank(message = "The Journal Visibility Shouldn't Be Empty")
    private Visibility visibility;

    @NotNull(message = "The Journal Publisher Shouldn't Be Null")
    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "publisher")
    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Interaction> interactions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    @JoinColumn(name = "journal")
    private List<Media> medias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reShare")
    @JdbcTypeCode(SqlTypes.JSON)
    private Journal reShare;

    @ManyToMany(mappedBy = "contributs")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<User> contributors;

    public Journal(String content, User publisher) {
        this.publisher = publisher;
        this.content = content;
    }

}