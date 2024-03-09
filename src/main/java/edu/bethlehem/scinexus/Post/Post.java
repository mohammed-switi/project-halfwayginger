package edu.bethlehem.scinexus.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import edu.bethlehem.scinexus.User.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
public class Post {
    private @Id @GeneratedValue Long id;
    private Long postId;
    private String content;
    private Visibility visibility;
    @JdbcTypeCode(SqlTypes.JSON)
    private User publisher;
    private Integer interactionCount;
    private Integer opinionsCount;
    @JdbcTypeCode(SqlTypes.JSON)
    private Post reShare;

    public Post(Long postId, String content, Visibility visibility, User publisher, Integer interactionCount,
            Integer opinionsCount, Post reShare) {
        this.postId = postId;
        this.content = content;
        this.visibility = visibility;
        this.publisher = publisher;
        this.interactionCount = interactionCount;
        this.opinionsCount = opinionsCount;
        this.reShare = reShare;
    }

    public Post() {
    }
}