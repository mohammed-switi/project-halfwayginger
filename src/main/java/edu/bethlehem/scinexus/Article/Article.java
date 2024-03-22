package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.Journal;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Article extends Journal {

    private @Id @GeneratedValue Long id;

    @NotNull(message = "The Article Subject Shouldn't Be Null")
    @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    private String subject;

    @NotNull(message = "The Article title Shouldn't Be Null")
    @NotBlank(message = "The Article title Shouldn't Be Empty")
    private String title;

    public Article(String title, String description, String subject, User publisherId) {
        super(description, publisherId);
        this.title = title;
        this.subject = subject;

    }

    public Article() {
    }
}