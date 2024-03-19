package edu.bethlehem.scinexus.Media;

import edu.bethlehem.scinexus.Journal.Journal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Column()
    private @Id @GeneratedValue Long mediaId;

    @Enumerated(EnumType.STRING)
    private MediaType type;
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal")
    private Journal ownerJournal;

    public Media(MediaType type, String path) {
        this.type = type;
        this.path = path;
    }

    public Media() {
    }
}