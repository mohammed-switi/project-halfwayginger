package edu.bethlehem.scinexus.Media;

import java.sql.SQLType;

import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.type.SqlTypes;

import edu.bethlehem.scinexus.Post.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Column(name = "media_id")
    private @Id @GeneratedValue Long mediaId;
    private MediaType type;
    private String path;

    // @OneToMany(mappedBy = "media")
    // @JdbcTypeCode(SqlTypes.JSON)
    // private Post ownerPost;

    public Media(Long mediaId, MediaType type, String path) {
        this.mediaId = mediaId;
        this.type = type;
        this.path = path;
    }

    public Media() {
    }
}