package edu.bethlehem.scinexus.Opinion;

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
    private @Id @GeneratedValue Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    private Journal journal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", cascade = CascadeType.ALL)
    private List<Opinion> opinions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "opinion")
    private List<Interaction> interactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reOpinion")
    @JdbcTypeCode(SqlTypes.JSON)
    private Opinion reOpinion;

    public Opinion(String content) {

        this.content = content;
    }

    public Opinion() {
    }
}