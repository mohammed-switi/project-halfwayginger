package edu.bethlehem.scinexus.Interaction;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Interaction {

    private @Id @GeneratedValue Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    private InteractionType type;

    @ManyToOne
    @JoinColumn(name = "opinion")
    @JsonIgnore
    @JsonManagedReference
    private Opinion opinion;

    @ManyToOne
    @JoinColumn(name = "journal")
    @JsonIgnore
    @JsonManagedReference
    private Journal journal;

    @ManyToOne
    @JoinColumn(name = "user")
    @JsonManagedReference
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