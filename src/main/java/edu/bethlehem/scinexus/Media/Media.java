package edu.bethlehem.scinexus.Media;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Column(name = "id")
    private @Id @GeneratedValue Long mediaId;
    private MediaType type;
    private String path;

    public Media(Long mediaId, MediaType type, String path) {
        this.mediaId = mediaId;
        this.type = type;
        this.path = path;
    }

    public Media() {
    }
}