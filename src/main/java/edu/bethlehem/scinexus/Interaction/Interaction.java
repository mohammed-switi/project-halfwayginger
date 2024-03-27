package edu.bethlehem.scinexus.Interaction;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Interaction {

    private @Id @GeneratedValue Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    private InteractionType type;

    @ManyToOne
    @JoinColumn(name = "opinion")
    @JsonIgnore
    private Opinion opinion;

    @ManyToOne
    @JoinColumn(name = "journal")
    @JsonIgnore
    private Journal journal;

    @ManyToOne
    @JoinColumn(name = "user")
    private User interactorUser;

    // This constructor Should be removed
    public Interaction(InteractionType type, User interactorUser) {

        this.type = type;
        this.interactorUser = interactorUser;
    }

    public Interaction(InteractionType type, User interactorUser, Journal journal) {

        this.type = type;
        this.interactorUser = interactorUser;
        this.journal = journal;
    }

    public Interaction(InteractionType type, User interactorUser, Opinion opinion) {

        this.type = type;
        this.interactorUser = interactorUser;
        this.opinion = opinion;
    }

    public Interaction() {
    }
}