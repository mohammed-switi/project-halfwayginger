package edu.bethlehem.scinexus.Opinion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Journal.Journal;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Opinion {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "The Opinion Content Can't Be Null")
    @NotBlank(message = "The Opinion Content Can't Be Empty")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Fetch Type Has been Changed from Lazy To Eager, Because When I request one opinion there is an error, and this is how I solved it
    @JoinColumn(name = "journal")
    @NotNull(message = "The Opinion Reference Journal Shouldn't Be Null")
    @JsonManagedReference
    private Journal journal;

    @NotNull(message = "Opinion Creation Date Can't be Null")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reOpinion")
    @JdbcTypeCode(SqlTypes.JSON)
    private Opinion reOpinion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "opinion")
    private List<Interaction> interactions;



    public Opinion(String content) {

        this.content = content;
    }

    public Opinion() {
    }
}