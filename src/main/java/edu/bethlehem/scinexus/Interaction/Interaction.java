package edu.bethlehem.scinexus.Interaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Interaction {
    private @Id @GeneratedValue Long id;
    private Long interactionId;
    private InteractionType type;

    public Interaction(Long interactionId, InteractionType type) {
        this.interactionId = interactionId;
        this.type = type;
    }

    public Interaction() {
    }
}