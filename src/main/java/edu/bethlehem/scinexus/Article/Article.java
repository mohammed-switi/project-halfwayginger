package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.Journal;

import edu.bethlehem.scinexus.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Article extends Journal {

    private @Id @GeneratedValue Long id;

    @NotNull(message = "The Article Subject Shouldn't Be Null")
    @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    private String subject;

    @NotNull(message = "The Article title Shouldn't Be Null")
    @NotBlank(message = "The Article title Shouldn't Be Empty")
    private String title;

    public Article(String title, String content, String subject, User publisher) {
        super(content, publisher);
        this.title = title;
        this.subject = subject;

    }

    public Article() {
    }
}