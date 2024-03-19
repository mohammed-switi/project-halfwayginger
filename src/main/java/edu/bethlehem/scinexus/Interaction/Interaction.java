package edu.bethlehem.scinexus.Interaction;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Opinion.Opinion;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class Interaction {
    private @Id @GeneratedValue Long id;
    private Long interactionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    private InteractionType type;

    @ManyToOne
    @JoinColumn(name = "opinion")
    private Opinion opinion;

    @ManyToOne
    @JoinColumn(name = "journal")
    private Journal journal;

    public Interaction(Long interactionId, InteractionType type) {
        this.interactionId = interactionId;
        this.type = type;
    }

    public Interaction() {
    }
}