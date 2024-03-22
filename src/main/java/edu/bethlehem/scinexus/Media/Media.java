package edu.bethlehem.scinexus.Media;

import edu.bethlehem.scinexus.Journal.Journal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Column()
    private @Id @GeneratedValue Long mediaId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The Media Type Shouldn't Be Null")
    @NotBlank(message = "The Media Type Shouldn't Be Empty")
    private MediaType type;

    @NotNull(message = "The Media Path Shouldn't Be Null")
    @NotBlank(message = "The Media Path Shouldn't Be Empty")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    @NotNull(message = "The Media Owner Shouldn't Be Null")
    @NotBlank(message = "The Media Owner Shouldn't Be Empty")
    private Journal ownerJournal;

    public Media(MediaType type, String path) {
        this.type = type;
        this.path = path;
    }

    public Media() {
    }
}