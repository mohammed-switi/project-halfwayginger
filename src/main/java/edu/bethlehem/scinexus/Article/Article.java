package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.Journal;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Article extends Journal {

    private @Id @GeneratedValue Long id;
    private String subject;

    private String title;

    public Article(String title, String description, String subject, User publisher) {
        super(description, publisher);
        this.title = title;
        this.subject = subject;

    }

    public Article() {
    }
}