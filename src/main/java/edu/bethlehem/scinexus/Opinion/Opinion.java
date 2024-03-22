package edu.bethlehem.scinexus.Opinion;

import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Journal.Journal;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Entity
public class Opinion {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "The Opinion Content Can't Be Null")
    @NotBlank(message = "The Opinion Content Can't Be Empty")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @NotNull(message = "The Opinion Reference Journal Shouldn't Be Null")
    @NotBlank(message = "The Opinion Reference Journal Be Empty")
    private Journal journal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reOpinion")
    @JdbcTypeCode(SqlTypes.JSON)
    private Opinion reOpinion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
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