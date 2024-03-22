package edu.bethlehem.scinexus.Post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import edu.bethlehem.scinexus.Journal.Journal;

import edu.bethlehem.scinexus.User.User;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Post extends Journal {
    private @Id @GeneratedValue Long id;

    public Post(String content, User publisherId) {
        super(content, publisherId);

    }

    public Post() {
    }
}