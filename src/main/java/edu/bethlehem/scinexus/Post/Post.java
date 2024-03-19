package edu.bethlehem.scinexus.Post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import edu.bethlehem.scinexus.Journal.Journal;

import edu.bethlehem.scinexus.User.User;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Post extends Journal {
    private @Id @GeneratedValue Long id;

    public Post(String content, User publisher) {
        super(content, publisher);

    }

    public Post() {
    }
}