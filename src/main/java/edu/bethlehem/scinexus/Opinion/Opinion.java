package edu.bethlehem.scinexus.Opinion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Opinion {
    private @Id @GeneratedValue Long id;
    private Long opinionId;
    private String content;

    public Opinion(Long opinionId, String content) {
        this.opinionId = opinionId;
        this.content = content;
    }

    public Opinion() {
    }
}