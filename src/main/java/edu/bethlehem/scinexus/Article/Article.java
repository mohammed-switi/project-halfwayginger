package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Article extends Journal {
    private @Id @GeneratedValue Long id;
    private String content;

    public Article(String name, String description, String subject, String title, String language, User publisher,
            Integer noOfPages, String visibility, Organization validatedBy, String content) {
        super(name, description, subject, title, language, publisher, noOfPages, visibility);
        this.content = content;

    }

    public Article() {
    }
}